package com.example.messenger;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class UserViewModel extends ViewModel implements UserRepository.OnUserAvaialbleInFireStore {

    MutableLiveData<List<UserModel>> mutableLiveData = new MutableLiveData<>();

    // instance of userrepo

    UserRepository userRepository = new UserRepository(this);

    public UserViewModel() {
        userRepository.getUserInFireStore();
    }



    public LiveData<List<UserModel>> getAllTheStupidUsers() {
        return  mutableLiveData;

    }

    @Override
    public void ShowListOfUser(List<UserModel> userModelList) {

        mutableLiveData.setValue(userModelList);
    }
}
