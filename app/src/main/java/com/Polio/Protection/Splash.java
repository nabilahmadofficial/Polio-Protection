package com.Polio.Protection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.Polio.Protection.admin.Adminhome;
import com.Polio.Protection.team.Teamhome;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Splash extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                boolean connected = false;
                ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

                    mAuth = FirebaseAuth.getInstance();
                    if (mAuth.getCurrentUser() != null) {

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                        String uid = mAuth.getInstance().getCurrentUser().getUid();

                        ref.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {


                                Log.i("Hello" , String.valueOf(dataSnapshot));
                                String name = dataSnapshot.child("type").getValue().toString();

                                if(name.equals("admin")) {
                                    Intent homeintent = new Intent(Splash.this , Adminhome.class);
                                    startActivity(homeintent);
                                    finish();
                                }
                                else if(name.equals("team")) {
                                    Intent homeintent = new Intent(Splash.this , Teamhome.class);
                                    startActivity(homeintent);
                                    finish();
                                }
                                else {
                                    Toast.makeText(Splash.this, "Something is wrong..", Toast.LENGTH_LONG).show();
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    }
                    else {
                        Intent homeintent = new Intent(Splash.this , Login.class);
                        startActivity(homeintent);
                        finish();
                    }
                    connected = true;
                }
                else {
                    Intent homeintent1 = new Intent(Splash.this , Error.class);
                    startActivity(homeintent1);
                    finish();
                }
                connected = false;
            }
        },SPLASH_TIME_OUT);
    }
}
