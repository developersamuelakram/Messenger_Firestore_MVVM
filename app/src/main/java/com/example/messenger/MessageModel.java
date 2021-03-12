package com.example.messenger;

public class MessageModel {

    String sender, receiver, time, message;


    public MessageModel() {
    }

    public MessageModel(String sender, String receiver, String time, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.time = time;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
