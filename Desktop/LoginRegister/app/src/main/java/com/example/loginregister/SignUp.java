package com.example.loginregister;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    TextInputEditText signUpName, signUpEmail, signUpPassword, signUpPasswordConfirm;
    AppCompatButton btnSignUp;
    ImageView signUpImage;
    TextView textAddImage;
    FrameLayout layoutImage;
    FirebaseAuth mAuth;
    String encodeImage;
    FirebaseFirestore mStore;
    ProgressBar progressBar;
    TextView signInNow;

    boolean valid = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        signUpImage = findViewById(R.id.signUpImage);
        layoutImage = findViewById(R.id.layoutImage);
        textAddImage = findViewById(R.id.textAddImage);
        signUpName = findViewById(R.id.signUpName);
        signUpEmail = findViewById(R.id.signUpEmail);
        signUpPassword = findViewById(R.id.signUpPassword);
        signUpPasswordConfirm = findViewById(R.id.signUpPasswordConfirm);
        btnSignUp = findViewById(R.id.btnSignUp);
        progressBar = findViewById(R.id.progessBar);
        signInNow = findViewById(R.id.signInNow);
        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        signInNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignIn.class);
                startActivity(intent);
                finish();
            }
        });

        layoutImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                pickImage.launch(intent);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String name, email, password, confirmPassword;
                name = String.valueOf(signUpName.getText());
                email = String.valueOf(signUpEmail.getText());
                password = String.valueOf(signUpPassword.getText());
                confirmPassword = String.valueOf(signUpPasswordConfirm.getText());
                if (encodeImage == null) {
                    progressBar.setVisibility(View.GONE);
                    textAddImage.setTextColor(Color.RED);
                    Toast.makeText(SignUp.this, "Select Image Profile", Toast.LENGTH_SHORT).show();
                    valid = false;
                }
                if (TextUtils.isEmpty(name)) {
                    signUpName.setError("Blank Name");
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(SignUp.this, "Enter Name", Toast.LENGTH_SHORT).show();
                    valid = false;

                }
                if (TextUtils.isEmpty(email)) {
                    signUpEmail.setError("Blank Email");
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(SignUp.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    valid = false;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    signUpEmail.setError("Invalid email");
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(SignUp.this, "Enter a valid email", Toast.LENGTH_SHORT).show();
                    valid = false;
                }
                if (TextUtils.isEmpty(password)) {
                    signUpPassword.setError("Blank Password");
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(SignUp.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    valid = false;
                }
                if (TextUtils.isEmpty(confirmPassword)) {
                    signUpPasswordConfirm.setError("Blank Password Confirm");
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(SignUp.this, "Enter Password Confirm", Toast.LENGTH_SHORT).show();
                    valid = false;

                }
                if (!confirmPassword.equals(password)) {
                    signUpPasswordConfirm.setError("Password confirm is not match");
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(SignUp.this, "Password confirm is not match", Toast.LENGTH_SHORT).show();
                    valid = false;

                }
                if (valid) {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        DocumentReference df = mStore.collection("Users").document(user.getUid());
                                        Map<String, Object> userInfo = new HashMap<>();
                                        userInfo.put("name", name);
                                        userInfo.put("email", email);
                                        userInfo.put("password", password);
                                        userInfo.put("isUser", "1");
                                        userInfo.put("image", encodeImage);

                                        df.set(userInfo);
                                        Toast.makeText(SignUp.this, "Account created",
                                                Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(),SignIn.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

                                        switch (errorCode) {

                                            case "ERROR_INVALID_CUSTOM_TOKEN":
                                                Toast.makeText(SignUp.this, "The custom token format is incorrect. Please check the documentation.", Toast.LENGTH_LONG).show();
                                                break;

                                            case "ERROR_CUSTOM_TOKEN_MISMATCH":
                                                Toast.makeText(SignUp.this, "The custom token corresponds to a different audience.", Toast.LENGTH_LONG).show();
                                                break;

                                            case "ERROR_INVALID_CREDENTIAL":
                                                Toast.makeText(SignUp.this, "The supplied auth credential is malformed or has expired.", Toast.LENGTH_LONG).show();
                                                break;

                                            case "ERROR_INVALID_EMAIL":
                                                Toast.makeText(SignUp.this, "The email address is badly formatted.", Toast.LENGTH_LONG).show();
                                                break;

                                            case "ERROR_WRONG_PASSWORD":
                                                Toast.makeText(SignUp.this, "The password is invalid or the user does not have a password.", Toast.LENGTH_LONG).show();
                                                break;

                                            case "ERROR_USER_MISMATCH":
                                                Toast.makeText(SignUp.this, "The supplied credentials do not correspond to the previously signed in user.", Toast.LENGTH_LONG).show();
                                                break;

                                            case "ERROR_REQUIRES_RECENT_LOGIN":
                                                Toast.makeText(SignUp.this, "This operation is sensitive and requires recent authentication. Log in again before retrying this request.", Toast.LENGTH_LONG).show();
                                                break;

                                            case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                                                Toast.makeText(SignUp.this, "An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.", Toast.LENGTH_LONG).show();
                                                break;

                                            case "ERROR_EMAIL_ALREADY_IN_USE":
                                                Toast.makeText(SignUp.this, "The email address is already in use by another account.   ", Toast.LENGTH_LONG).show();
                                                break;


                                            case "ERROR_WEAK_PASSWORD":
                                                Toast.makeText(SignUp.this, "The given password is invalid.", Toast.LENGTH_LONG).show();
                                                break;

                                        }
                                    }
                                }
                            });
                }
            }
        });
    }

    private String encodeImage(Bitmap bitmap) {
        int previewWidth = 150;
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
                            signUpImage.setImageBitmap(bitmap);
                            textAddImage.setVisibility(View.GONE);
                            encodeImage = encodeImage(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );
}