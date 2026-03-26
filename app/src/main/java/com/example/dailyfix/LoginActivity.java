package com.example.dailyfix;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private Button btnLogin, btnGoogleLogin, btnTabConnexion, btnTabInscription;
    private TextView tvForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialisation des vues
        initViews();
        setupListeners();
    }

    private void initViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoogleLogin = findViewById(R.id.btnGoogleLogin);
        btnTabConnexion = findViewById(R.id.btnTabConnexion);
        btnTabInscription = findViewById(R.id.btnTabInscription);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
    }

    private void setupListeners() {
        if (btnTabInscription != null) {
            btnTabInscription.setOnClickListener(v -> navigateToRegister());
        }

        if (btnTabConnexion != null) {
            btnTabConnexion.setOnClickListener(v -> {
                // Already on login screen.
            });
        }

        if (btnLogin != null) {
            btnLogin.setOnClickListener(v -> login());
        }

        if (btnGoogleLogin != null) {
            btnGoogleLogin.setOnClickListener(v ->
                    Toast.makeText(LoginActivity.this, "Connexion Google en cours...", Toast.LENGTH_SHORT).show());
        }

        if (tvForgotPassword != null) {
            tvForgotPassword.setOnClickListener(v ->
                    Toast.makeText(LoginActivity.this, "Redirection vers récupération de mot de passe", Toast.LENGTH_SHORT).show());
        }
    }

    private void navigateToRegister() {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        finish();
    }

    private void login() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User(email, password);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<LoginResponse> call = apiService.loginUser(user);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    handleLoginSuccess(response.body());
                } else {
                    Toast.makeText(LoginActivity.this, "Email ou mot de passe incorrect", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Erreur réseau : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleLoginSuccess(LoginResponse loginResponse) {
        Toast.makeText(LoginActivity.this, "Connexion réussie !", Toast.LENGTH_SHORT).show();

        User loggedUser = loginResponse.getUser();
        String userName = extractUserName(loggedUser);
        String role = loggedUser != null ? loggedUser.getRole() : null;
        boolean isAdmin = "admin".equalsIgnoreCase(role);

        SessionManager.saveUserSession(LoginActivity.this, userName, loginResponse.getToken(), role);

        Intent intent = new Intent(LoginActivity.this, isAdmin ? AdminActivity.class : HomeActivity.class);
        intent.putExtra("userName", userName);
        intent.putExtra("userRole", role);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private String extractUserName(User user) {
        if (user == null) {
            return "";
        }
        if (user.getName() != null && !user.getName().trim().isEmpty()) {
            return user.getName().split(" ")[0];
        }
        if (user.getEmail() != null && !user.getEmail().trim().isEmpty()) {
            return user.getEmail().split("@")[0];
        }
        return "";
    }
}
