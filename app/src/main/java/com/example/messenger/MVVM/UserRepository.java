package com.example.messenger.MVVM;

import androidx.annotation.Nullable;

import com.example.messenger.Model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {



    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    OnUserAvaialbleInFireStore interfaceofusers;
    String userid;
    List<UserModel> userModelList = new ArrayList<>();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


    public UserRepository(OnUserAvaialbleInFireStore interfaceofusers) {
        this.interfaceofusers = interfaceofusers;
    }

    public void getUserInFireStore() {


        userid = firebaseAuth.getCurrentUser().getUid();

        firestore.collection("Users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                userModelList.clear();


                assert value != null;
                for (DocumentSnapshot ds: value.getDocuments()) {


                    UserModel userModel = ds.toObject(UserModel.class);

                    assert userModel != null;
                    if (!userid.equals(userModel.getUserid())) {


                        userModelList.add(userModel);
                        // we are adding the list of users into the interface
                        interfaceofusers.ShowListOfUser(userModelList);



                    }




                }



            }
        });









    }

    public interface OnUserAvaialbleInFireStore{
        // this interface will provide us the userlist whereever we are going to use usererpo class.
        void ShowListOfUser(List<UserModel> userModelList);




    }
}
