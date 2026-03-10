package com.android.cobasiapin.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.android.cobasiapin.R;
import com.android.cobasiapin.model.User;
import com.android.cobasiapin.utils.FirebaseHelper;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etEmail, etPassword;
    private Button btnRegister;
    private TextView tvLoginSekarang;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseHelper.getAuth();

        initViews();
        setListeners();
    }

    private void initViews() {
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLoginSekarang = findViewById(R.id.tvLoginSekarang);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Membuat akun...");
        progressDialog.setCancelable(false);
    }

    private void setListeners() {

        // Tombol Register
        btnRegister.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (validateInput(username, email, password)) {
                registerUser(username, email, password);
            }
        });

        // Link ke Login
        tvLoginSekarang.setOnClickListener(v -> {
            finish(); // Kembali ke LoginActivity
        });
    }

    private boolean validateInput(String username, String email, String password) {
        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Username atau NIM tidak boleh kosong");
            etUsername.requestFocus();
            return false;
        }
        if (username.length() < 3) {
            etUsername.setError("Username minimal 3 karakter");
            etUsername.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email tidak boleh kosong");
            etEmail.requestFocus();
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Format email tidak valid");
            etEmail.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password tidak boleh kosong");
            etPassword.requestFocus();
            return false;
        }
        if (password.length() < 6) {
            etPassword.setError("Password minimal 6 karakter");
            etPassword.requestFocus();
            return false;
        }
        return true;
    }

    private void registerUser(String username, String email, String password) {
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            saveUserToDatabase(
                                    firebaseUser.getUid(),
                                    username,
                                    email
                            );
                        }
                    } else {
                        progressDialog.dismiss();
                        String errorMsg = "Registrasi gagal. ";
                        if (task.getException() != null) {
                            String exMsg = task.getException().getMessage();
                            if (exMsg != null && exMsg.contains("email address is already")) {
                                errorMsg += "Email sudah terdaftar.";
                            } else if (exMsg != null && exMsg.contains("badly formatted")) {
                                errorMsg += "Format email tidak valid.";
                            } else {
                                errorMsg += "Silakan coba lagi.";
                            }
                        }
                        Toast.makeText(RegisterActivity.this,
                                errorMsg,
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void saveUserToDatabase(String uid, String username, String email) {
        // Buat object User baru
        User newUser = new User(
                uid,
                username,    // nama sementara = username
                username,    // nim sementara = username (bisa diupdate di profil)
                email,
                "",          // programStudi kosong dulu
                "mahasiswa"  // role default
        );

        // Simpan ke Firebase Realtime Database
        DatabaseReference userRef = FirebaseHelper.getDatabaseRef("users/" + uid);
        userRef.setValue(newUser)
                .addOnCompleteListener(task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this,
                                "Akun berhasil dibuat! Silakan login.",
                                Toast.LENGTH_SHORT).show();
                        // Kembali ke Login
                        Intent intent = new Intent(
                                RegisterActivity.this,
                                LoginActivity.class
                        );
                        intent.setFlags(
                                Intent.FLAG_ACTIVITY_NEW_TASK |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK
                        );
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this,
                                "Gagal menyimpan data. Coba lagi.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}