package com.example.pushnotifangelia;

import static com.example.pushnotifangelia.Constants.SHARED_PREF;
import static com.google.firebase.messaging.Constants.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.pushnotifangelia.databinding.ActivityMainBinding;
import com.example.pushnotifangelia.model.NotifContentModel;
import com.example.pushnotifangelia.model.PushNotifModel;
import com.example.pushnotifangelia.retrofit.ApiClient;
import com.example.pushnotifangelia.retrofit.ApiService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getFirebaseInstanceId();
        initListener();
    }

    private void getFirebaseInstanceId() {
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        FCMService.sharedPref = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if(!task.isSuccessful()) {
                        Log.e(Constants.TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    // Get new FCM registration token
                    String token = task.getResult();
                    Log.d(Constants.TAG, "Token: " + token);
                    // Save or Update token to Shared Pref
                    FCMService.setToken(token);
                    // Display Token in Screen Layout
                    binding.tvCurrentDeviceId.setText(token);
                });
    }

    private void initListener() {
        binding.ibCopy.setOnClickListener(v -> {
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("TEXT", binding.tvCurrentDeviceId.getText().toString());
            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(this, "Token Copied!", Toast.LENGTH_SHORT).show();
        });

        binding.btnSendNotification.setOnClickListener(v -> {
            String title = binding.etTitle.getText().toString();
            String message = binding.etMessage.getText().toString();
            String recipientToken = binding.etReceiverDeviceId.getText().toString();
            if(!title.isEmpty() && !message.isEmpty() && !recipientToken.isEmpty()) {
                sendNotification(recipientToken, title, message);
            }
            else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendNotification(String recipientToken, String title, String message) {
        PushNotifModel data = new PushNotifModel(new NotifContentModel(title, message), recipientToken);

        ApiService apiService = ApiClient.getApiService();
        Call<ResponseBody> call = apiService.postNotification(data);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(!response.isSuccessful()) {
                    Log.d(Constants.TAG, String.valueOf(response.code()));
                    return;
                }
                binding.etTitle.setText("");
                binding.etMessage.setText("");
                Toast.makeText(MainActivity.this, "Message Pushed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(Constants.TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}