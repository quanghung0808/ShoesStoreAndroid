package com.example.loginregister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeAdmin extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    Button logoutAdmin;
    TextView adminDetails;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);

        firebaseAuth = FirebaseAuth.getInstance();
        logoutAdmin = findViewById(R.id.logoutAdmin);
        adminDetails = findViewById(R.id.adminDetails);
        firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser == null) {
            Intent intent = new Intent(getApplicationContext(),SignIn.class);
            startActivity(intent);
            finish();
        }
        else {
            adminDetails.setText(firebaseUser.getEmail());
        }

        logoutAdmin.setOnClickListener(new View.OnClickListener() {
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