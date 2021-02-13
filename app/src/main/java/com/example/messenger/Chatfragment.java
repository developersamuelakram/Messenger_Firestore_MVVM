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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.messenger.MVVM.MessageViewModel;
import com.example.messenger.Model.MessageModel;
import com.example.messenger.MyAdapter.MessageAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class Chatfragment extends Fragment {


    TextView userName;
    CircleImageView imageView;
    int position;
    String friendid, imageUrl, username;
    EditText messageet_text;
    ImageButton sendMessage;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    String message, userid;
    ImageView backButton;
    NavController navController;
    RecyclerView recyclerView;
    MessageViewModel viewModel;
    MessageAdapter mAdapter;

    public Chatfragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragmentschat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        userName = view.findViewById(R.id.chatFragUserName);
        imageView = view.findViewById(R.id.chatImageUser);

        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new MessageAdapter();

        firebaseAuth = FirebaseAuth.getInstance();
        navController = Navigation.findNavController(view);
        backButton = view.findViewById(R.id.chatBackbUTTON);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navController.navigate(R.id.action_chatFragment_to_userFragment);
            }
        });

        userid = firebaseAuth.getCurrentUser().getUid();


        firestore = FirebaseFirestore.getInstance();
        sendMessage = view.findViewById(R.id.sendMessage);
        messageet_text = view.findViewById(R.id.etMessage);



        position = ChatfragmentArgs.fromBundle(getArguments()).getPosition();
        friendid = ChatfragmentArgs.fromBundle(getArguments()).getFriendid();
        imageUrl = ChatfragmentArgs.fromBundle(getArguments()).getImageUrl();
        username = ChatfragmentArgs.fromBundle(getArguments()).getUsername();

        userName.setText(username);



        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                message = messageet_text.getText().toString();

                if (message.isEmpty()) {

                    messageet_text.setError("Type something Bitch");
                } else {

                    SendMessage(friendid, message, userid);
                    messageet_text.setText(" ");



                }



            }
        });

    }

    private void SendMessage(String friendid, String message, String userid) {



        SimpleDateFormat formatter= new SimpleDateFormat( "HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String currenttime = formatter.format(date);


        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("sender", userid);
        hashMap.put("receiver", friendid);
        hashMap.put("message", message);
        hashMap.put("time", currenttime);

        firestore.collection("Messages").document(currenttime).set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        firestore.collection("Users").document(friendid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {


                    DocumentSnapshot tasks = task.getResult();

                    String photoid = tasks.getString("imageUrl");


                    Glide.with(getContext()).load(photoid).centerCrop().into(imageView);
                }

            }
        });

        viewModel = new ViewModelProvider(getActivity()).get(MessageViewModel.class);
        viewModel.getMessageFromFireSTORE(friendid);

        viewModel.ReturnMyMessages().observe(getViewLifecycleOwner(), new Observer<List<MessageModel>>() {
            @Override
            public void onChanged(List<MessageModel> messageModels) {

                mAdapter.setMessageModelList(messageModels);
                recyclerView.setAdapter(mAdapter);
            }
        });
    }


    @Override
    public void onStop() {
        super.onStop();

        viewModel.resetAllShit();
    }

}