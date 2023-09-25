package com.Polio.Protection.admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;

import com.Polio.Protection.Login;
import com.Polio.Protection.R;
import com.Polio.Protection.admin.Campaign.AdminCampaign;
import com.Polio.Protection.admin.Children.Children;
import com.Polio.Protection.admin.Teams.AdminTeams;
import com.Polio.Protection.admin.ViewResult.AdminViewResultSelectCampaign;
import com.Polio.Protection.admin.Worker.AdminWorker;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class Adminhome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    CardView one, two, three, four, five;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        one = findViewById(R.id.view_all_result_activity);
        two = findViewById(R.id.add_update_campaign_activity);
        three = findViewById(R.id.add_update_worker_activity);
        four = findViewById(R.id.add_update_team_activity);
        five = findViewById(R.id.admin_children_info_activity);

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Adminhome.this, AdminViewResultSelectCampaign.class);
                startActivity(intent);
            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Adminhome.this, AdminCampaign.class);
                startActivity(intent);
            }
        });
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Adminhome.this, AdminWorker.class);
                startActivity(intent);
            }
        });
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Adminhome.this, AdminTeams.class);
                startActivity(intent);
            }
        });
        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Adminhome.this, Children.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_view_all_result) {

            Intent intent = new Intent(Adminhome.this, AdminViewResultSelectCampaign.class);
            startActivity(intent);

        } else if (id == R.id.nav_menu_campaign) {

            Intent intent = new Intent(Adminhome.this, AdminCampaign.class);
            startActivity(intent);

        } else if (id == R.id.nav_menu_worker) {

            Intent intent = new Intent(Adminhome.this, AdminWorker.class);
            startActivity(intent);

        } else if (id == R.id.nav_menu_team) {

            Intent intent = new Intent(Adminhome.this, AdminTeams.class);
            startActivity(intent);
        } else if (id == R.id.nav_menu_children) {

            Intent intent = new Intent(Adminhome.this, Children.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {

            new AlertDialog.Builder(Adminhome.this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String TAG = "somethigs";
                            FirebaseAuth mAuth = FirebaseAuth.getInstance();
                            try {

                                mAuth.signOut();
                                startActivity(new Intent(Adminhome.this, Login.class));
                                finish();

                            } catch (Exception e) {
                                Log.e(TAG, "onClick: Exception " + e.getMessage(), e);
                            }
                        }
                    }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            }).show();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private long lastPressedTime;
    private static final int PERIOD = 2000;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            switch (event.getAction()) {
                case KeyEvent.ACTION_DOWN:
                    if (event.getDownTime() - lastPressedTime < PERIOD) {
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Press again to exit.",
                                Toast.LENGTH_SHORT).show();
                        lastPressedTime = event.getEventTime();
                    }
                    return true;
            }
        }
        return false;
    }
}
