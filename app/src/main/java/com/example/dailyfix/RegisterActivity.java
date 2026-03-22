package com.example.dailyfix;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity  extends AppCompatActivity {
    private EditText etName, etEmail, etPassword;
    private Button btnRegister, btnTabConnexion, btnTabInscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialisation des vues
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnTabConnexion = findViewById(R.id.btnTabConnexion);
        btnTabInscription = findViewById(R.id.btnTabInscription);

        // Navigation vers l'écran de connexion
        if (btnTabConnexion != null) {
            btnTabConnexion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish(); // Ferme l'activité actuelle
                }
            });
        }

        // Action d'inscription
        if (btnRegister != null) {
            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    registerUser();
                }
            });
        }
    }

    private void registerUser() {
        try {
            if (etName == null || etEmail == null || etPassword == null) {
                Toast.makeText(this, "Erreur d'affichage. Réessayez.", Toast.LENGTH_SHORT).show();
                return;
            }

            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            // Appel de l'API Node.js via Retrofit
            User user = new User(name, email, password);
            ApiService apiService = ApiClient.getClient().create(ApiService.class);

            Call<RegisterResponse> call = apiService.registerUser(user);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                    if (isDestroyed()) return;
                    try {
                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(RegisterActivity.this, "Inscription réussie !", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            String errorMsg = "Erreur lors de l'inscription";
                            if (response.code() == 400) {
                                errorMsg = "Données invalides ou utilisateur déjà existant";
                            }
                            Toast.makeText(RegisterActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e("RegisterActivity", "Erreur onResponse", e);
                        Toast.makeText(RegisterActivity.this, "Erreur : " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {
                    Log.e("RegisterActivity", "Erreur réseau", t);
                    if (!isDestroyed()) {
                        Toast.makeText(RegisterActivity.this, "Erreur réseau : " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } catch (Exception e) {
            Log.e("RegisterActivity", "Erreur registerUser", e);
            Toast.makeText(this, "Erreur : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
