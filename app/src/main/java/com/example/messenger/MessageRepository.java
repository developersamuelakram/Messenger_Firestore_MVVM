package com.example.messenger;

import androidx.annotation.Nullable;

import com.example.messenger.MessageModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MessageRepository {

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userid;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    List<MessageModel> messageModelList = new ArrayList<>();
    OnMessageAdded interfaceformessages;

    public MessageRepository(OnMessageAdded interfaceformessages) {
        this.interfaceformessages = interfaceformessages;
    }

    public void getAllMessages(String friend) {

        userid = firebaseAuth.getCurrentUser().getUid();



        firestore.collection("Messages").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                messageModelList.clear();
                for (DocumentSnapshot ds: value.getDocuments()) {


                    MessageModel messageModel = ds.toObject(MessageModel.class);

                    // we only want to display conversation between two users
                    // since every user will have diffferent conversation
                    if (messageModel.getSender().equals(userid) && messageModel.getReceiver().equals(friend) ||
                    messageModel.getReceiver().equals(userid) && messageModel.getSender().equals(friend)) {


                        messageModelList.add(messageModel);
                        interfaceformessages.MessagesfROMFIRESTORE(messageModelList);





                    }





                }


            }
        });





    }

    public interface OnMessageAdded{

        void MessagesfROMFIRESTORE(List<MessageModel> messageModels);

    }



}
