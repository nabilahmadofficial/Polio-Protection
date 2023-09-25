package com.Polio.Protection.team;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.Polio.Protection.Login;
import com.Polio.Protection.R;
import com.Polio.Protection.team.CheckReport.TeamCheckReportCheckCampaign;
import com.Polio.Protection.team.Children.TeamChildren;
import com.Polio.Protection.team.ColdChainBox.TeamColdChainBox;
import com.Polio.Protection.team.Info.TeamInfo;
import com.Polio.Protection.team.MarkChildren.TeamMarkChildrenCheckCampaign;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;


public class Teamhome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    CardView one, two, three, four, five;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        one = findViewById(R.id.mark_children_activity);
        two = findViewById(R.id.add_update_children_activity);
        three = findViewById(R.id.cold_chain_activity);
        four = findViewById(R.id.check_report_children_activity);
        five = findViewById(R.id.team_info_children_activity);

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Teamhome.this, TeamMarkChildrenCheckCampaign.class);
                startActivity(intent);
            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Teamhome.this, TeamChildren.class);
                startActivity(intent);
            }
        });
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Teamhome.this, TeamColdChainBox.class);
                startActivity(intent);
            }
        });
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Teamhome.this, TeamCheckReportCheckCampaign.class);
                startActivity(intent);
            }
        });
        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Teamhome.this, TeamInfo.class);
                startActivity(intent);
            }
        });

//        location();
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_mark_children) {

            Intent intent = new Intent(Teamhome.this, TeamMarkChildrenCheckCampaign.class);
            startActivity(intent);

        } else if (id == R.id.nav_add_drop_children) {

            Intent intent = new Intent(Teamhome.this, TeamChildren.class);
            startActivity(intent);

        } else if (id == R.id.nav_cold_chain_box) {

            Intent intent = new Intent(Teamhome.this, TeamColdChainBox.class);
            startActivity(intent);

        } else if (id == R.id.nav_check_report) {

            Intent intent = new Intent(Teamhome.this, TeamCheckReportCheckCampaign.class);
            startActivity(intent);

        } else if (id == R.id.nav_info) {

            Intent intent = new Intent(Teamhome.this, TeamInfo.class);
            startActivity(intent);

        } else if (id == R.id.nav_logout) {

            new AlertDialog.Builder(Teamhome.this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String TAG = "somethigs";
                            FirebaseAuth mAuth = FirebaseAuth.getInstance();
                            try {

                                mAuth.signOut();
                                startActivity(new Intent(Teamhome.this, Login.class));
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
//    public void location()
//    {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
//                    == PackageManager.PERMISSION_GRANTED) {
//                Log.v("Main", "Permission is granted");
//                //return true;
//            } else {
//                Log.v("Main", "Permission is revoked");
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//                //return false;
//            }
//        }
//        else {
//            int permission = PermissionChecker.checkSelfPermission(getApplicationContext().getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION );
//            if (permission == PermissionChecker.PERMISSION_GRANTED) {
//                // good to go
//                Log.v("Main", "Permission is granted");
//            } else {
//                // permission not granted, you decide what to do
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//            }
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION)
//                    == PackageManager.PERMISSION_GRANTED) {
//                Log.v("Main", "Permission is granted");
//                //return true;
//            } else {
//                Log.v("Main", "Permission is revoked");
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//                //return false;
//            }
//        }
//        else {
//            int permission = PermissionChecker.checkSelfPermission(getApplicationContext().getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION );
//            if (permission == PermissionChecker.PERMISSION_GRANTED) {
//                // good to go
//                Log.v("Main", "Permission is granted");
//            } else {
//                // permission not granted, you decide what to do
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//            }
//        }
//    }

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
