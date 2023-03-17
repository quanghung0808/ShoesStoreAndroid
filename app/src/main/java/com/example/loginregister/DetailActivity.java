package com.example.loginregister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.loginregister.model.GenericShoes;
import com.squareup.picasso.Picasso;

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

    }
}