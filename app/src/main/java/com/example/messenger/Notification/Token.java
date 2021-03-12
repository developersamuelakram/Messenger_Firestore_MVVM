package com.example.messenger.Notification;

public class Token {
// AN FCM token is a registration token. An ID issued by
    // the GCM // connection servers to the client app that allow it receive the message


    String token;

    public Token() {
    }

    public Token(String token) {
        this.token = token;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    @Override
    public String toString() {
        return "Token{" +
                "token='" + token + '\'' +
                '}';
    }
}
