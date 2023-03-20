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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginregister.model.GenericShoes;
import com.example.loginregister.model.ShoesOrder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    public List<ShoesOrder> shoesOrderList = new ArrayList<>();
    private TextView tvName;
    private TextView tvPrice;
    private ImageView igImage;
    private Spinner spinner;
    private EditText editText;
    private int size;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        EditText editText = findViewById(R.id.spinnerColor);
        Intent intent = getIntent();
        Spinner spinner = (Spinner) findViewById(R.id.spinnerSize);
        int shoeId = intent.getIntExtra("shoe_id", 0);
        String shoeName = intent.getStringExtra("shoe_name");
        double shoePrice = intent.getDoubleExtra("shoe_price", 0.0);
        String shoeImage = intent.getStringExtra("shoe_image");
        int categoryId = intent.getIntExtra("shoe_CategoryId", 0);
        GenericShoes genericShoes = new GenericShoes(shoeId, shoeName, shoePrice, shoeImage, categoryId);
        List<Integer> list = new ArrayList<Integer>();
        for (int i=34; i<=44; i++){
            list.add(i);
        }
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                size = list.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
                if (Integer.parseInt(String.valueOf(editText.getText()))>0){
                    ShoesOrder shoesOrder = new ShoesOrder(genericShoes, size,Integer.parseInt(String.valueOf(editText.getText())));
                    shoesOrderList.add(shoesOrder);
                    Toast.makeText(DetailActivity.this, "Add Success", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(DetailActivity.this, "Add Fail", Toast.LENGTH_SHORT).show();
                }
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