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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.android.cobasiapin.R;
import com.android.cobasiapin.utils.FirebaseHelper;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TextView tvLupaPassword, tvDaftarSekarang;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseHelper.getAuth();

        // Cek jika sudah login
        if (FirebaseHelper.isLoggedIn()) {
            goToMain();
            return;
        }

        initViews();
        setListeners();
    }

    private void initViews() {
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvLupaPassword = findViewById(R.id.tvLupaPassword);
        tvDaftarSekarang = findViewById(R.id.tvDaftarSekarang);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sedang masuk...");
        progressDialog.setCancelable(false);
    }

    private void setListeners() {

        btnLogin.setOnClickListener(v -> {
            String input = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (validateInput(input, password)) {
                // Cek apakah input berupa email atau username/NIM
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(input).matches()) {
                    // Langsung login pakai email
                    loginWithEmail(input, password);
                } else {
                    // Cari email berdasarkan username/NIM di database
                    findEmailByUsername(input, password);
                }
            }
        });

        tvDaftarSekarang.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

        tvLupaPassword.setOnClickListener(v -> {
            String input = etUsername.getText().toString().trim();
            if (TextUtils.isEmpty(input)) {
                Toast.makeText(this,
                        "Masukkan email Anda terlebih dahulu",
                        Toast.LENGTH_SHORT).show();
            } else if (android.util.Patterns.EMAIL_ADDRESS.matcher(input).matches()) {
                resetPassword(input);
            } else {
                Toast.makeText(this,
                        "Gunakan email untuk reset password",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Cari email berdasarkan username atau NIM di database
    private void findEmailByUsername(String username, String password) {
        progressDialog.setMessage("Mencari akun...");
        progressDialog.show();

        DatabaseReference usersRef = FirebaseHelper.getDatabaseRef("users");
        usersRef.orderByChild("nim").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // Ketemu by NIM
                            for (DataSnapshot userSnap : snapshot.getChildren()) {
                                String email = userSnap.child("email").getValue(String.class);
                                if (email != null) {
                                    loginWithEmail(email, password);
                                    return;
                                }
                            }
                        } else {
                            // Coba cari by nama/username
                            findEmailByNama(username, password);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this,
                                "Error: " + error.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void findEmailByNama(String username, String password) {
        DatabaseReference usersRef = FirebaseHelper.getDatabaseRef("users");
        usersRef.orderByChild("nama").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        progressDialog.dismiss();
                        if (snapshot.exists()) {
                            for (DataSnapshot userSnap : snapshot.getChildren()) {
                                String email = userSnap.child("email").getValue(String.class);
                                if (email != null) {
                                    loginWithEmail(email, password);
                                    return;
                                }
                            }
                        } else {
                            Toast.makeText(LoginActivity.this,
                                    "Akun tidak ditemukan. Coba gunakan email.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this,
                                "Error: " + error.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loginWithEmail(String email, String password) {
        if (!progressDialog.isShowing()) {
            progressDialog.setMessage("Sedang masuk...");
            progressDialog.show();
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            Toast.makeText(LoginActivity.this,
                                    "Login berhasil! Selamat datang.",
                                    Toast.LENGTH_SHORT).show();
                            goToMain();
                        }
                    } else {
                        String errorMsg = "Login gagal. ";
                        if (task.getException() != null) {
                            String exMsg = task.getException().getMessage();
                            if (exMsg != null && exMsg.contains("password")) {
                                errorMsg += "Password salah.";
                            } else if (exMsg != null && exMsg.contains("no user")) {
                                errorMsg += "Akun tidak ditemukan.";
                            } else if (exMsg != null && exMsg.contains("blocked")) {
                                errorMsg += "Terlalu banyak percobaan. Coba lagi nanti.";
                            } else {
                                errorMsg += "Periksa kembali email dan password Anda.";
                            }
                        }
                        Toast.makeText(LoginActivity.this,
                                errorMsg,
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void resetPassword(String email) {
        progressDialog.setMessage("Mengirim email reset password...");
        progressDialog.show();

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this,
                                "Email reset password dikirim ke " + email,
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(LoginActivity.this,
                                "Gagal kirim email. Pastikan email terdaftar.",
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private boolean validateInput(String input, String password) {
        if (TextUtils.isEmpty(input)) {
            etUsername.setError("Username, NIM, atau Email tidak boleh kosong");
            etUsername.requestFocus();
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

    private void goToMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}