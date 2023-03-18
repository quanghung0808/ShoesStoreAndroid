package com.example.loginregister;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class UpdateActivity extends AppCompatActivity {
    String encodeImage;
    ShapeableImageView updateImage;
    FirebaseFirestore mStore;
    FirebaseAuth mAuth;
    EditText nameUpdate;
    TextView emailUpdate;
    Button updateProfile, logout;
    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        updateImage = findViewById(R.id.updateImage);
        updateProfile = findViewById(R.id.updateProfile);
        logout = findViewById(R.id.logout);
        nameUpdate = findViewById(R.id.nameUpdate);
        emailUpdate = findViewById(R.id.emailUpdate);

        mStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        updateImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });

        DocumentReference df = mStore.collection("Users").document(mAuth.getCurrentUser().getUid());
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                byte[] bytes = Base64.decode(documentSnapshot.getString("image"), Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                encodeImage = encodeImage(bitmap);
                updateImage.setImageBitmap(bitmap);
                nameUpdate.setText(documentSnapshot.getString("name"));
                emailUpdate.setText(documentSnapshot.getString("email"));
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(),SignIn.class);
                startActivity(intent);
                finish();
            }
        });

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                df.update("image", encodeImage, "name", nameUpdate.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(UpdateActivity.this, "Update Successfully.", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(UpdateActivity.this, "Update Fail.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }


    private String encodeImage(Bitmap bitmap) {
        int previewWidth = 400;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            updateImage.setImageBitmap(bitmap);
                            encodeImage = encodeImage(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );
}