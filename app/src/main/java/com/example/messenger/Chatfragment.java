package com.example.messenger;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.messenger.Notification.APISERVICESHIT;
import com.example.messenger.Notification.Client;
import com.example.messenger.Notification.Data;
import com.example.messenger.Notification.Response;
import com.example.messenger.Notification.Sender;
import com.example.messenger.Notification.Token;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;

import static android.content.ContentValues.TAG;


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
    String token;
    String useridfortoken;
    APISERVICESHIT apiserviceshit;
    boolean notify = false;
    String nameofsender;

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


        apiserviceshit = Client.getRetrofit("https://fcm.googleapis.com/").create(APISERVICESHIT.class);



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


        Intent intent = getActivity().getIntent();
        friendid = intent.getStringExtra("friendid");

        position = ChatfragmentArgs.fromBundle(getArguments()).getPosition();
        friendid = ChatfragmentArgs.fromBundle(getArguments()).getFriendid();
        imageUrl = ChatfragmentArgs.fromBundle(getArguments()).getImageUrl();
        username = ChatfragmentArgs.fromBundle(getArguments()).getUsername();

      //  userName.setText(username);



        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                notify = true;


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



        firestore.collection("Users").document(userid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                UserModel userModel = documentSnapshot.toObject(UserModel.class);
                nameofsender = userModel.getUsername();

                Log.d(TAG, "onSuccess: " + nameofsender);


                if (notify) {

                    SendNotification(friendid, nameofsender, message);
                    Log.d(TAG, "notify: " + nameofsender);


                }

                notify = false;


            }
        });





    }

    private void SendNotification(String friendid, String nameofsender, String message) {


        useridfortoken = firebaseAuth.getCurrentUser().getUid();

        firestore.collection("Tokens").document(friendid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {


                assert value != null;
                Token objectotoken = value.toObject(Token.class);
                assert objectotoken != null;
                token = objectotoken.getToken();

                Log.d(TAG, "onEvent: " + token);


                Data data = new Data(useridfortoken, R.mipmap.ic_launcher, message, "New Message From " + nameofsender, friendid);

                Sender sender = new Sender(data, token);
                apiserviceshit.sendNotification(sender).enqueue(new Callback<Response>() {
                    @Override
                    public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                        if (response.code() == 200) {

                            if (response.body().success != 1) {

                                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();

                            }


                        }
                    }

                    @Override
                    public void onFailure(Call<Response> call, Throwable t) {

                    }
                });




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
                    String name = tasks.getString("username");

                    userName.setText(name);


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