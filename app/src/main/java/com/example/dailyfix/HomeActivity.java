package com.example.dailyfix;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.drawerlayout.widget.DrawerLayout;

public class HomeActivity extends AppCompatActivity {
    private TextView tvGreeting;
    private TextView btnStatsToggle;
    private LinearLayout statsContent;
    private DrawerLayout drawerLayout;
    private boolean statsExpanded = true;
    private String userName;
    private boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applySavedTheme();
        setContentView(R.layout.activity_home);

        userName = getIntent().getStringExtra("userName");
        String userRole = getIntent().getStringExtra("userRole");
        if (userName == null || userName.isEmpty()) {
            userName = SessionManager.getUserName(this);
        }
        if (userRole == null || userRole.isEmpty()) {
            userRole = SessionManager.getUserRole(this);
        }
        isAdmin = "admin".equalsIgnoreCase(userRole);
        if (userName == null || userName.isEmpty()) {
            userName = getString(R.string.app_name);
        }

        tvGreeting = findViewById(R.id.tvGreeting);
        tvGreeting.setText(getString(R.string.home_greeting_user, userName));

        btnStatsToggle = findViewById(R.id.btnStatsToggle);
        statsContent = findViewById(R.id.statsContent);
        btnStatsToggle.setOnClickListener(v -> toggleStatsSection());

        setupSidebar();
        setupNavbar();
        setupDashboardButtons();
        updateStatsWithPlaceholderData();
    }

    private void setupSidebar() {
        drawerLayout = findViewById(R.id.homeDrawerLayout);
        Button btnSidebarHome = findViewById(R.id.btnSidebarHome);
        Button btnSidebarTasks = findViewById(R.id.btnSidebarTasks);
        Button btnSidebarHealth = findViewById(R.id.btnSidebarHealth);
        Button btnSidebarFinance = findViewById(R.id.btnSidebarFinance);
        Button btnSidebarSocial = findViewById(R.id.btnSidebarSocial);
        Button btnSidebarWellness = findViewById(R.id.btnSidebarWellness);
        Button btnSidebarAdmin = findViewById(R.id.btnSidebarAdmin);
        Button btnSidebarLogout = findViewById(R.id.btnSidebarLogout);

        if (btnSidebarHome != null) btnSidebarHome.setOnClickListener(v -> closeSidebar());
        if (btnSidebarAdmin != null) {
            btnSidebarAdmin.setVisibility(isAdmin ? View.VISIBLE : View.GONE);
            btnSidebarAdmin.setOnClickListener(v -> {
                closeSidebar();
                startActivity(new Intent(HomeActivity.this, AdminActivity.class));
            });
        }
        if (btnSidebarTasks != null) btnSidebarTasks.setOnClickListener(v -> sidebarFeature("Tasks"));
        if (btnSidebarHealth != null) btnSidebarHealth.setOnClickListener(v -> sidebarFeature("Health"));
        if (btnSidebarFinance != null) btnSidebarFinance.setOnClickListener(v -> sidebarFeature("Finance"));
        if (btnSidebarSocial != null) btnSidebarSocial.setOnClickListener(v -> sidebarFeature("Social"));
        if (btnSidebarWellness != null) btnSidebarWellness.setOnClickListener(v -> sidebarFeature("Wellness"));
        if (btnSidebarLogout != null) btnSidebarLogout.setOnClickListener(v -> logout());
    }

    private void sidebarFeature(String feature) {
        closeSidebar();
        Toast.makeText(this, feature + " - Bientot disponible", Toast.LENGTH_SHORT).show();
    }

    private void closeSidebar() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(Gravity.START)) {
            drawerLayout.closeDrawer(Gravity.START);
        }
    }

    private void applySavedTheme() {
        int nightMode = SessionManager.getNightMode(this, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        AppCompatDelegate.setDefaultNightMode(nightMode);
    }

    private void setupNavbar() {
        ImageButton btnMenu = findViewById(R.id.btnMenu);
        ImageButton btnThemeToggle = findViewById(R.id.btnThemeToggle);
        LinearLayout profileContainer = findViewById(R.id.profileContainer);
        TextView tvAvatar = findViewById(R.id.tvAvatar);
        TextView tvProfileName = findViewById(R.id.tvProfileName);

        if (btnMenu != null) {
            btnMenu.setOnClickListener(v -> {
                if (drawerLayout != null) {
                    drawerLayout.openDrawer(Gravity.START);
                }
            });
        }
        if (btnThemeToggle != null) {
            updateThemeIcon(btnThemeToggle);
            btnThemeToggle.setOnClickListener(v -> toggleTheme(btnThemeToggle));
        }
        if (profileContainer != null) {
            profileContainer.setOnClickListener(v -> showProfileMenu(profileContainer));
        }
        if (tvAvatar != null) {
            tvAvatar.setText(getUserInitials(userName));
        }
        if (tvProfileName != null) {
            tvProfileName.setText(userName);
        }
    }

    private String getUserInitials(String name) {
        if (name == null || name.isEmpty()) return "?";
        String[] parts = name.trim().split("\\s+");
        if (parts.length >= 2) {
            return String.valueOf(parts[0].charAt(0)) + parts[1].charAt(0);
        }
        return name.length() >= 2 ? name.substring(0, 2) : name.substring(0, 1);
    }

    private void updateThemeIcon(ImageButton btn) {
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        boolean isDark = nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
        btn.setImageResource(isDark ? R.drawable.ic_theme_light : R.drawable.ic_theme_dark);
    }

    private void toggleTheme(ImageButton btn) {
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        int newMode = nightModeFlags == Configuration.UI_MODE_NIGHT_YES
                ? AppCompatDelegate.MODE_NIGHT_NO
                : AppCompatDelegate.MODE_NIGHT_YES;
        AppCompatDelegate.setDefaultNightMode(newMode);
        SessionManager.setNightMode(this, newMode);
        recreate();
    }

    private void showProfileMenu(View anchor) {
        PopupMenu popup = new PopupMenu(this, anchor);
        popup.getMenuInflater().inflate(R.menu.menu_profile, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_settings) {
                Toast.makeText(this, R.string.nav_settings, Toast.LENGTH_SHORT).show();
                return true;
            }
            if (item.getItemId() == R.id.action_logout) {
                logout();
                return true;
            }
            return false;
        });
        popup.show();
    }

    private void logout() {
        SessionManager.clearSession(this);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void toggleStatsSection() {
        statsExpanded = !statsExpanded;
        statsContent.setVisibility(statsExpanded ? View.VISIBLE : View.GONE);
        btnStatsToggle.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                statsExpanded ? R.drawable.ic_chevron_down : R.drawable.ic_chevron_right, 0);
    }

    private void setupDashboardButtons() {
        setButtonClickListener(R.id.btnManageTasks, getString(R.string.home_manage_tasks));
        setButtonClickListener(R.id.btnUpdateHealth, getString(R.string.home_update_health));
        setButtonClickListener(R.id.btnAddExpense, getString(R.string.home_add_expense));
        setButtonClickListener(R.id.btnPlanTask, getString(R.string.home_plan_task));
        setButtonClickListener(R.id.btnNewEvent, getString(R.string.home_new_event));
        setButtonClickListener(R.id.btnNewGoal, getString(R.string.home_new_goal));
    }

    private void setButtonClickListener(int buttonId, String featureName) {
        Button btn = findViewById(buttonId);
        if (btn != null) {
            btn.setOnClickListener(v ->
                    Toast.makeText(HomeActivity.this,
                            featureName + " - Bientôt disponible",
                            Toast.LENGTH_SHORT).show());
        }
    }

    private void updateStatsWithPlaceholderData() {
        setStatText(R.id.tvTotalExpenses, "0");
        setStatText(R.id.tvSavingsGoals, "0");
        setStatText(R.id.tvActiveGoals, "0");
        setStatText(R.id.tvHealthRecords, "0");
        setStatText(R.id.tvSocialEvents, "0");
        setStatText(R.id.tvTotalTasks, "0");
        setStatText(R.id.tvTodayTasks, "0");
        setStatText(R.id.tvHealthScore, "0%");
        setStatText(R.id.tvRemainingBalance, "0");
        setStatText(R.id.tvUpcomingEvents, "0");
        setStatText(R.id.tvTaskProgress, getString(R.string.home_completed_today_progress));
    }

    private void setStatText(int id, String text) {
        TextView tv = findViewById(id);
        if (tv != null) {
            tv.setText(text);
        }
    }

}
