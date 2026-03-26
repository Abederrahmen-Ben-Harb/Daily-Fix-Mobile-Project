package com.example.dailyfix;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import android.view.Gravity;

public class AdminActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        setupAdminSidebar();

        Button btnBackHome = findViewById(R.id.btnBackHome);
        btnBackHome.setOnClickListener(v -> {
            startActivity(new Intent(AdminActivity.this, HomeActivity.class));
            finish();
        });

        setText(R.id.tvUsersValue, "120");
        setText(R.id.tvUsersDetails, "+12 ce mois, +4 cette semaine");
        setText(R.id.tvTasksValue, "450");
        setText(R.id.tvTasksDetails, "312 complétées, 69% de réussite");
        setText(R.id.tvEventsValue, "38");
        setText(R.id.tvEventsDetails, "9 événements à venir");
        setText(R.id.tvHealthValue, "97");
        setText(R.id.tvHealthDetails, "66 repas, 31 activités");
        setText(R.id.tvFinanceValue, "210");
        setText(R.id.tvFinanceDetails, "90 budgets, 120 objectifs");
        setText(R.id.tvWellnessValue, "73");
        setText(R.id.tvWellnessDetails, "40 journaux, 33 objectifs");
        setText(R.id.tvSocialValue, "15");
        setText(R.id.tvSocialDetails, "Événements sociaux");
    }

    private void setupAdminSidebar() {
        drawerLayout = findViewById(R.id.adminDrawerLayout);
        ImageButton btnAdminMenu = findViewById(R.id.btnAdminMenu);
        Button btnAdminNavDashboard = findViewById(R.id.btnAdminNavDashboard);
        Button btnAdminNavUsers = findViewById(R.id.btnAdminNavUsers);
        Button btnAdminNavBackHome = findViewById(R.id.btnAdminNavBackHome);
        Button btnAdminLogout = findViewById(R.id.btnAdminLogout);

        if (btnAdminMenu != null) {
            btnAdminMenu.setOnClickListener(v -> {
                if (drawerLayout != null) drawerLayout.openDrawer(Gravity.START);
            });
        }
        if (btnAdminNavDashboard != null) {
            btnAdminNavDashboard.setOnClickListener(v -> closeSidebar());
        }
        if (btnAdminNavUsers != null) {
            btnAdminNavUsers.setOnClickListener(v -> {
                closeSidebar();
                Toast.makeText(this, "Gestion des utilisateurs - Bientot disponible", Toast.LENGTH_SHORT).show();
            });
        }
        if (btnAdminNavBackHome != null) {
            btnAdminNavBackHome.setOnClickListener(v -> {
                startActivity(new Intent(AdminActivity.this, HomeActivity.class));
                finish();
            });
        }
        if (btnAdminLogout != null) {
            btnAdminLogout.setOnClickListener(v -> logout());
        }
    }

    private void closeSidebar() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(Gravity.START)) {
            drawerLayout.closeDrawer(Gravity.START);
        }
    }

    private void logout() {
        SessionManager.clearSession(this);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void setText(int id, String value) {
        TextView tv = findViewById(id);
        if (tv != null) {
            tv.setText(value);
        }
    }
}
