package com.Polio.Protection;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.Polio.Protection.admin.Adminhome;
import com.Polio.Protection.team.Teamhome;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Error extends AppCompatActivity {

    Button btn;
    private FirebaseAuth mAuth;

    ProgressBar pr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);

        pr = findViewById(R.id.progressBar);
        pr.setVisibility(View.INVISIBLE);

        btn = findViewById(R.id.ErrorBtn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean connected = false;
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

                    pr.setVisibility(View.VISIBLE);
                    mAuth = FirebaseAuth.getInstance();
                    if (mAuth.getCurrentUser() != null) {

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                        String uid = mAuth.getInstance().getCurrentUser().getUid();

                        ref.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String name = dataSnapshot.child("type").getValue().toString();

                                if (name.equals("admin")) {
                                    Intent homeintent = new Intent(Error.this, Adminhome.class);
                                    startActivity(homeintent);
                                    pr.setVisibility(View.INVISIBLE);
                                    finish();
                                } else if (name.equals("team")) {
                                    Intent homeintent = new Intent(Error.this, Teamhome.class);
                                    startActivity(homeintent);
                                    pr.setVisibility(View.INVISIBLE);
                                    finish();
                                } else {
                                    Toast.makeText(Error.this, "Something is wrong..", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    } else {
                        Intent homeintent = new Intent(Error.this, Login.class);
                        startActivity(homeintent);
                        pr.setVisibility(View.INVISIBLE);
                        finish();
                    }
                    connected = true;
                } else {
                    new AlertDialog.Builder(Error.this)
                            .setTitle("No Internet Connection")
                            .setMessage("You need to have Mobile Data or wifi to access this. Press ok to Exit")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                                }
                            }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                            .show();
                }
                connected = false;
            }
        });
    }
}
