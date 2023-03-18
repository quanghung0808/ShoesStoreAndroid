package com.example.loginregister;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.loginregister.model.GenericShoes;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.widget.Button;
import android.view.View;
import android.app.Notification;
import android.app.NotificationManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.util.Date;

public class DetailActivity extends AppCompatActivity {

    private TextView tvName;
    private TextView tvPrice;
    private ImageView igImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();

        int shoeId = intent.getIntExtra("shoe_id", 0);
        String shoeName = intent.getStringExtra("shoe_name");
        double shoePrice = intent.getDoubleExtra("shoe_price", 0.0);
        String shoeImage = intent.getStringExtra("shoe_image");

        // Initialize the TextViews to display the shoe information
        tvName = findViewById(R.id.name);
        tvPrice = findViewById(R.id.price);
        igImage = findViewById(R.id.image);
        // Set the text of the TextViews to display the shoe information
        tvName.setText(String.format("%s", shoeName));
        tvPrice.setText(String.format("%f VND", shoePrice));
        Picasso.get().load(shoeImage)
                .into(igImage);

        if (getSupportActionBar() != null) {
            setTitle("Detail Page");
        }
        //notification
        Button btnSendNotification = findViewById(R.id.add);
        btnSendNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNotification();
            }
        });



    }


    //notification
    private void sendNotification(){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round);
        android.app.Notification notification = new Notification.Builder(this).setContentTitle("Title push notifitaction")
                .setContentText("Message push notification")
                .setSmallIcon(R.drawable.baseline_notifications_active_24)
                .setLargeIcon(bitmap)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null){
            notificationManager.notify(getNotificationId(), notification);
        }
    }
    //notification
    private int getNotificationId(){
        return (int)  new Date().getTime();
    }
}