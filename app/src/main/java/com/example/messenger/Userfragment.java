package com.example.messenger;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.messenger.MVVM.UserViewModel;
import com.example.messenger.Model.UserModel;
import com.example.messenger.MyAdapter.UserAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class Userfragment extends Fragment implements UserAdapter.OnUserClicked {

    Button logOut;
    FirebaseAuth firebaseAuth;
    NavController navController;
    RecyclerView recyclerView;
    UserAdapter mAdapter;
    UserViewModel viewModel;
    CircleImageView circleImageView;
    TextView userNameLoggedIn;
    FirebaseFirestore firestore;
    String userid;

    public Userfragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        navController = Navigation.findNavController(view);

        circleImageView = view.findViewById(R.id.imageViewUser);


        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_userFragment_to_profileFragment);
            }
        });
        userNameLoggedIn = view.findViewById(R.id.usernameonuserfragment);

        logOut = view.findViewById(R.id.logoutButton);

        recyclerView = view.findViewById(R.id.recyclerViewUser);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new UserAdapter(this);


        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                if (firebaseUser!=null) {



                    String theuserid = firebaseUser.getUid();

                    firebaseAuth.signOut();
                    // because we want to keep the user offline once its logged out
                    firestore.collection("Users").document(theuserid).update("status", "Offline").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });


                    navController.navigate(R.id.action_userFragment_to_loginFragment);

                    Toast.makeText(getContext(), "Logged Out", Toast.LENGTH_SHORT).show();
                }




            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String userid = firebaseAuth.getCurrentUser().getUid();

        firestore.collection("Users").document(userid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {

                    DocumentSnapshot userModel = task.getResult();

                    String name = userModel.getString("username");
                    String imageUrl = userModel.getString("imageUrl");

                    userNameLoggedIn.setText(name);
                    Glide.with(getContext()).load(imageUrl).centerCrop().into(circleImageView);


                }

            }
        });


        viewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
        viewModel.getAllTheStupidUsers().observe(getViewLifecycleOwner(), new Observer<List<UserModel>>() {
            @Override
            public void onChanged(List<UserModel> userModels) {

                mAdapter.setUserModelList(userModels);
                recyclerView.setAdapter(mAdapter);

            }
        });


    }



    public void setStatus(String status) {


        FirebaseUser useroffirebase = FirebaseAuth.getInstance().getCurrentUser();

        if (useroffirebase!=null) {

            userid = useroffirebase.getUid();

        }


        firestore.collection("Users").document(userid).update("status", status).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });




    }

    @Override
    public void UserisCicked(int position, List<UserModel> userModels) {

        UserfragmentDirections.ActionUserFragmentToChatFragment action = UserfragmentDirections.actionUserFragmentToChatFragment();

        UserModel userModel = userModels.get(position);
        String friendid = userModel.getUserid();
        String imageUrl = userModel.getImageUrl();
        String username = userModel.getUsername();

        action.setFriendid(friendid);
        action.setImageUrl(imageUrl);
        action.setPosition(position);
        action.setUsername(username);

        navController.navigate(action);

    }


    @Override
    public void onResume() {
        super.onResume();
        setStatus("Online");
    }

    @Override
    public void onPause() {
        super.onPause();
        setStatus("Offline");



    }
}