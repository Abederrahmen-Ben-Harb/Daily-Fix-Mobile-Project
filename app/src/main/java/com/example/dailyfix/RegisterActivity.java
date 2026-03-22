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
    private Button btnRegister, btnTabConnexion;

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

        // Navigation vers l'écran de connexion
        btnTabConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Ferme l'activité actuelle
            }
        });

        // Action d'inscription
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
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

        Call<User> call = apiService.registerUser(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Inscription réussie !", Toast.LENGTH_SHORT).show();
                    // Rediriger vers l'écran de connexion après succès
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Erreur *** lors de l'inscription", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Erreur réseau : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
