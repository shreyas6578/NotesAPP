package com.example.notesapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText email_box, password_box;
    private Button login, register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize UI elements
        email_box = findViewById(R.id.editTextText3);
        password_box = findViewById(R.id.editTextTextPassword);
        login = findViewById(R.id.button2);    // Assuming ID for login button is button2
        register = findViewById(R.id.button3); // Assuming ID for register button is button3

        // Register button click listener
        register.setOnClickListener(click -> {
            String email = email_box.getText().toString().trim();
            String password = password_box.getText().toString().trim();

            if (validateInput(email, password)) {
                registerUser(email, password);
            }
        });

        // Login button click listener
        login.setOnClickListener(click -> {
            String email = email_box.getText().toString().trim();
            String password = password_box.getText().toString().trim();

            if (validateInput(email, password)) {
                loginUser(email, password);
            }
        });
    }

    // Validate input fields for email and password
    private boolean validateInput(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(Login.this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // Register new user
    private void registerUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(Login.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                           // email_box.setText("");
                           // password_box.setText("");
                        } else {
                            Log.w("createUserWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Login existing user
    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(Login.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("isLoggedIn", true);
                            editor.apply();
                            // After login, go back to MainActivity
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.w("signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Login failed. Please check your credentials.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
