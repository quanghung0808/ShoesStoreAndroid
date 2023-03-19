package com.example.loginregister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Base64;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.loginregister.model.ChatMessage;
import com.example.loginregister.model.ChatUser;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChatAdmin extends AppCompatActivity {
    private FirebaseListAdapter<ChatUser> adapter;
    private List<ChatUser> chatUsers;
    private ListView lvChatAdmin;
    private final String DB_URL = "https://login-register-89b9f-default-rtdb.asia-southeast1.firebasedatabase.app";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_admin);
        lvChatAdmin = findViewById(R.id.lvChatAdmin);
        chatUsers = new ArrayList<>();
        loadData();
        lvChatAdmin.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(this, Chat.class);
            intent.putExtra("user", chatUsers.get(i));
            startActivity(intent);
        });
    }

    private void loadData() {
        Query query = FirebaseDatabase.getInstance(DB_URL).getReference().child("chats");
        FirebaseListOptions<ChatUser> options = new FirebaseListOptions.Builder<ChatUser>().setQuery(query, ChatUser.class).setLayout(R.layout.chat_item).build();
        adapter = new FirebaseListAdapter<ChatUser>(options) {
            @Override
            protected void populateView(View v, ChatUser model, int position) {
                // Get references to the views of chat_item.xml
                ShapeableImageView avatar = v.findViewById(R.id.avatar);
                TextView tvEmail = (TextView) v.findViewById(R.id.tvEmail);
                TextView tvName = (TextView) v.findViewById(R.id.tvName);
                TextView tvTime = (TextView) v.findViewById(R.id.tvTime);
                TextView tvMessage = (TextView) v.findViewById(R.id.tvMessage);
                // Set their text
                tvEmail.setText(model.getEmail());
                tvName.setText(model.getName());
                byte[] bytes = Base64.decode(model.getImage(), Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                avatar.setImageBitmap(bitmap);

                String emailPrefix = model.getEmail().split("@")[0];
                Query chatMessageQuery = FirebaseDatabase.getInstance(DB_URL).getReference().child("chats").child(emailPrefix).child("c").orderByChild("time").limitToLast(1);
                chatMessageQuery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
                            tvMessage.setText(chatMessage.getMessage());
                            // Format the date before showing it
                            tvTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                                    chatMessage.getTime()));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                chatUsers.add(model);
            }
        };
        adapter.startListening();
        lvChatAdmin.setAdapter(adapter);
    }
}