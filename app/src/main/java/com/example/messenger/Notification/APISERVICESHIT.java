package com.example.messenger.Notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APISERVICESHIT {


    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAGqaR4qs:APA91bGSJTt_ecH-esfmAzhpkCemx8mxNU3RYQFzfG9m4RQzHNiGlA_u5t5AUE0fpJtRmA_AtDjIlCtWAnJ4Eg0sRAzfTzj82fqhS_q83GROwIPauZ1MIP7Rlu6W6tHlOlPfh9q7cGV7"
    })

    @POST("fcm/send")
    Call<Response> sendNotification(@Body Sender body);


}
