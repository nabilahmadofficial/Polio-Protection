package com.Polio.Protection.admin.Children;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.Polio.Protection.R;
import com.Polio.Protection.admin.Map.AdminMap;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Date;

public class AdminChildrenVaccinationDeatils extends AppCompatActivity {

    ImageView backarrow, map;
    ProgressBar pr;
    String campaign_key, child_key;

    TextView child_name, team_id, mark_date, marke_time;
    ImageView child_image;

    String name_child, key_team, id_team, date_mark, time_mark, image_child, location_latitude, location_longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_children_vaccination_deatils);

        map = findViewById(R.id.map);
        backarrow = findViewById(R.id.backsign);
        backarrow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        pr = findViewById(R.id.progressBar);

        Intent intent = getIntent();
        campaign_key = intent.getStringExtra("campaign_key");
        child_key = intent.getStringExtra("children_key");

        child_name = findViewById(R.id.mark_children_child_name);
        team_id = findViewById(R.id.mark_children_team_id);
        mark_date = findViewById(R.id.mark_children_date);
        marke_time = findViewById(R.id.mark_children_time);
        child_image = findViewById(R.id.children_image);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Campaign").child(campaign_key).child("Children").child(child_key);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {

                    name_child = dataSnapshot.child("children_name").getValue().toString();
                    key_team = dataSnapshot.child("team_key").getValue().toString();
                    image_child = dataSnapshot.child("picture").getValue().toString();
                    location_latitude = dataSnapshot.child("location_latitude").getValue().toString();
                    location_longitude = dataSnapshot.child("location_longitude").getValue().toString();
                    String date_and_time = dataSnapshot.child("date_time").getValue().toString();

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/M/yyyy");
                    date_mark = dateFormat.format(new Date(date_and_time));
                    SimpleDateFormat dateFormat2 = new SimpleDateFormat("HH:mm:ss");
                    time_mark = dateFormat2.format(new Date(date_and_time));

                    fetch_team_id(name_child, key_team, image_child, date_mark, time_mark);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //pr.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Something was wrong !!!", Toast.LENGTH_SHORT).show();
            }
        });

        map.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                location(location_latitude, location_longitude);
            }
        });

    }

    private void fetch_team_id(final String name_child, String key_team, final String image_child, final String date_mark, final String time_mark) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(key_team);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    id_team = dataSnapshot.child("id").getValue().toString();

                    child_name.setText(name_child);
                    team_id.setText(id_team);
                    mark_date.setText(date_mark);
                    marke_time.setText(time_mark);
                    Picasso.get().load(image_child).into(child_image);
                    pr.setVisibility(View.INVISIBLE);
                } else {
                    pr.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Something was Wrong !!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pr.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Something was wrong !!!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void location(String location_latitude, String location_longitude) {

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            Intent intent = new Intent(AdminChildrenVaccinationDeatils.this, AdminMap.class);
            intent.putExtra("location_latitude", location_latitude);
            intent.putExtra("location_longitude", location_longitude);
            intent.putExtra("Action_map", "Single_show");
            getApplication().startActivity(intent);
        } else {
            on_internet_dailog();
        }
    }

    private void on_internet_dailog() {

        new AlertDialog.Builder(AdminChildrenVaccinationDeatils.this)
                .setTitle("No Internet Connection")
                .setMessage("You need to have Mobile Data or wifi to access this. Press ok")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).show();
    }
}
