package com.example.loginregister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.loginregister.model.ChatMessage;
import com.example.loginregister.model.ChatUser;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Chat extends AppCompatActivity {
    private FirebaseListAdapter<ChatMessage> adapter;
    private ListView lvChat;
    private EditText etChat;
    private Button btnChat;
    private ChatUser chatUser;

    private String currentName;
    private String DB_URL = "https://login-register-89b9f-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        lvChat = findViewById(R.id.lvChat);
        etChat = findViewById(R.id.etChat);
        btnChat = findViewById(R.id.btnSend);
        btnChat.setEnabled(false);
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        System.out.println(task.isSuccessful());
                        DocumentSnapshot snapshot = task.getResult();
                        System.out.println(snapshot);
                        if (snapshot.exists()) {
                            currentName = snapshot.getString("name");
                            if (snapshot.getString("isAdmin") != null) {
                                chatUser = (ChatUser) getIntent().getSerializableExtra("user");
                                displayMessages();
                            } else {
                                chatUser = new ChatUser();
                                chatUser.setName(snapshot.getString("name"));
                                chatUser.setEmail(snapshot.getString("email"));
                                chatUser.setImage(snapshot.getString("image"));
                                displayMessages();
                            }
                        }
                    }
                });
        btnChat.setOnClickListener(view -> sendMessage());
        etChat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.equals("")){
                    btnChat.setEnabled(false);
                } else {
                    btnChat.setEnabled(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.equals("")){
                    btnChat.setEnabled(false);
                } else {
                    btnChat.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() == 0){
                    btnChat.setEnabled(false);
                }
            }
        });
    }

    private void sendMessage() {
        String userPrefix = chatUser.getEmail().split("@")[0];
        FirebaseDatabase fb = FirebaseDatabase.getInstance(DB_URL);
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("email", chatUser.getEmail());
        userMap.put("name", chatUser.getName());
        userMap.put("image", chatUser.getImage());
        fb.getReference().child("chats").child(userPrefix).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setFrom(currentName);
                chatMessage.setMessage(etChat.getText().toString());
                chatMessage.setTime(new Date().getTime());
                fb.getReference().child("chats").child(userPrefix).child("c").push()
                        .setValue(chatMessage).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                // Clear the input
                                etChat.setText("");
                                displayMessages();
                            }
                        });
            }
        });
    }

    private void displayMessages() {
        Query query = FirebaseDatabase.getInstance(DB_URL).getReference().child("chats").child(chatUser.getEmail().split("@")[0]).child("c");
        FirebaseListOptions<ChatMessage> options = new FirebaseListOptions.Builder<ChatMessage>().setQuery(query, ChatMessage.class).setLayout(R.layout.message).build();
        adapter = new FirebaseListAdapter<ChatMessage>(options) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                System.out.println("CHECK: " + model);
                // Get references to the views of message.xml
                TextView tvName = (TextView) v.findViewById(R.id.tvName);
                TextView tvEmail = (TextView) v.findViewById(R.id.tvEmail);
                TextView tvTime = (TextView) v.findViewById(R.id.tvTime);
                TextView tvMessage = (TextView) v.findViewById(R.id.tvMessage);
                // Set their text
                tvName.setText(model.getFrom());
                tvEmail.setText(chatUser.getEmail());
                // Format the date before showing it
                tvTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getTime()));
                tvMessage.setText(model.getMessage());
            }
        };
        adapter.startListening();
        lvChat.setAdapter(adapter);
    }
}