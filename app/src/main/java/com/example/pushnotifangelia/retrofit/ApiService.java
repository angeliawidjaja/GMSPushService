package com.example.pushnotifangelia.retrofit;

import static com.example.pushnotifangelia.Constants.CONTENT_TYPE;
import static com.example.pushnotifangelia.Constants.SERVER_KEY;

import com.example.pushnotifangelia.model.PushNotifModel;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Angelia Widjaja on 21-Jan-22 16:53.
 */
public interface ApiService {
    @Headers({"Authorization: key=" + SERVER_KEY, "Content-Type:" + CONTENT_TYPE})
    @POST("/fcm/send")
    Call<ResponseBody> postNotification(@Body PushNotifModel notification);
}
