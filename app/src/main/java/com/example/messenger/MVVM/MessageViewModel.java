package com.example.messenger.MVVM;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.messenger.Model.MessageModel;

import java.util.List;

public class MessageViewModel  extends ViewModel implements MessageRepository.OnMessageAdded {



    MutableLiveData<List<MessageModel>> mutableLiveData = new MutableLiveData<>();
    MessageRepository repo = new MessageRepository(this);


    public MessageViewModel() {
    }

    public void getMessageFromFireSTORE(String friendid) {
        repo.getAllMessages(friendid);



    }


    public void resetAllShit() {

        mutableLiveData.postValue(null);

    }



    public LiveData<List<MessageModel>> ReturnMyMessages() {

        return  mutableLiveData;



    }
    @Override
    public void MessagesfROMFIRESTORE(List<MessageModel> messageModels) {
        mutableLiveData.setValue(messageModels);
    }
}
