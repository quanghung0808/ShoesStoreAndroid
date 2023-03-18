package com.example.loginregister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeUser extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    Button logoutUser;
    TextView userDetails;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user);

        firebaseAuth = FirebaseAuth.getInstance();
        logoutUser = findViewById(R.id.logoutUser);
        userDetails = findViewById(R.id.userDetails);
        firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser == null) {
            Intent intent = new Intent(getApplicationContext(),SignIn.class);
            startActivity(intent);
            finish();
        }
        else {
            userDetails.setText(firebaseUser.getEmail());
        }

        logoutUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(),SignIn.class);
                startActivity(intent);
                finish();
            }
        });
    }
}