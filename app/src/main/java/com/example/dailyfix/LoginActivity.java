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
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoogleLogin = findViewById(R.id.btnGoogleLogin);
        btnTabConnexion = findViewById(R.id.btnTabConnexion);
        btnTabInscription = findViewById(R.id.btnTabInscription);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        // Navigation vers l'inscription via l'onglet
        if (btnTabInscription != null) {
            btnTabInscription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                    finish();
                }
            });
        }

        // Navigation vers la connexion (déjà sur cet écran, mais pour la cohérence)
        if (btnTabConnexion != null) {
            btnTabConnexion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Déjà sur LoginActivity
                }
            });
        }

        // Gestion du clic sur le bouton de connexion
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                } else {
                    // Appel de l'API Node.js via Retrofit
                    User user = new User(email, password);
                    ApiService apiService = ApiClient.getClient().create(ApiService.class);

                    Call<LoginResponse> call = apiService.loginUser(user);
                    call.enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                LoginResponse loginResponse = response.body();
                                Toast.makeText(LoginActivity.this, "Connexion réussie !", Toast.LENGTH_SHORT).show();

                                String userName = "";
                                if (loginResponse.getUser() != null && loginResponse.getUser().getName() != null) {
                                    userName = loginResponse.getUser().getName().split(" ")[0];
                                } else if (loginResponse.getUser() != null && loginResponse.getUser().getEmail() != null) {
                                    userName = loginResponse.getUser().getEmail().split("@")[0];
                                }
                                String role = loginResponse.getUser() != null ? loginResponse.getUser().getRole() : null;
                                HomeActivity.saveUserSession(LoginActivity.this, userName, loginResponse.getToken(), role);
                                boolean isAdmin = loginResponse.getUser() != null
                                        && loginResponse.getUser().getRole() != null
                                        && "admin".equalsIgnoreCase(loginResponse.getUser().getRole());
                                Intent intent = new Intent(
                                        LoginActivity.this,
                                        isAdmin ? AdminActivity.class : HomeActivity.class
                                );
                                intent.putExtra("userName", userName);
                                intent.putExtra("userRole", role);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
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
            }
        });

        // Gestion du clic sur le bouton Google
        btnGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Connexion Google en cours...", Toast.LENGTH_SHORT).show();
            }
        });

        // Gestion du mot de passe oublié
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Redirection vers récupération de mot de passe", Toast.LENGTH_SHORT).show();
            }
        });

        // Navigation vers l'écran d'inscription (doublon supprimé si nécessaire, mais ici on garde la logique existante avec sécurité)
        if (btnTabInscription != null) {
            btnTabInscription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }
}
