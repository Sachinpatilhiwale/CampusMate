package com.mountreach.campusmanagementsystem.StudentDashboard;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mountreach.campusmanagementsystem.Adapter.ChatAdapter;
import com.mountreach.campusmanagementsystem.Model.ChatMessage;
import com.mountreach.campusmanagementsystem.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AiActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    EditText messageEditText;
    ImageButton sendButton;

    ChatAdapter adapter;
    List<ChatMessage> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai);

        recyclerView = findViewById(R.id.chatRecyclerView);
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);

        messageList = new ArrayList<>();

        adapter = new ChatAdapter(messageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        sendButton.setOnClickListener(v -> {

            String message = messageEditText.getText().toString().trim();

            if (!message.isEmpty()) {

                messageList.add(new ChatMessage(message, true));
                adapter.notifyDataSetChanged();

                messageEditText.setText("");

                callGeminiAPI(message);   // AI call
            }

        });
    }

    // 🔹 Gemini API Method (CLASS च्या आत)
    private void callGeminiAPI(String userMessage) {

        OkHttpClient client = new OkHttpClient();

        String apiKey = "AIzaSyDEziLxW2d9V-6LlzUszO397XN491GKaq8";

        String json = "{ \"contents\": [{ \"parts\": [{ \"text\": \"" + userMessage + "\" }] }] }";

        RequestBody body = RequestBody.create(
                json, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=" + apiKey)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {

                runOnUiThread(() ->
                        messageList.add(new ChatMessage("AI Error", false))
                );

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String responseData = response.body().string();

                runOnUiThread(() -> {

                    messageList.add(new ChatMessage(responseData, false));
                    adapter.notifyDataSetChanged();

                });
            }
        });
    }
}