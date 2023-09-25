package com.Polio.Protection.admin.ViewResult;

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

public class AdminViewResultChildrenVaccination extends AppCompatActivity {

    TextView Child_name, father_name, father_cnic, child_date_of_birth, child_gender, child_mark_date, child_mark_time, child_address, team_id;
    String name_Child, name_father, cnic_father, date_of_birth_child, gender_child, mark_date_child, mark_time_child, address_child, id_team, picture_child;
    String child_key, campaign_key, date_and_time, location_latitude, location_longitude, team_key;
    ImageView backarrow, map, children_image;
    ProgressBar pr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_view_result_children_vaccination);

        map = findViewById(R.id.map);
        backarrow = findViewById(R.id.backsign);
        backarrow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        pr = findViewById(R.id.progressBar);
        pr.setVisibility(View.VISIBLE);

        Child_name = findViewById(R.id.mark_children_child_name);
        father_name = findViewById(R.id.mark_children_father_name);
        father_cnic = findViewById(R.id.mark_children_father_cnic);
        child_date_of_birth = findViewById(R.id.mark_children_date_of_birth);
        child_gender = findViewById(R.id.mark_children_gender);
        child_mark_date = findViewById(R.id.mark_children_date);
        child_mark_time = findViewById(R.id.mark_children_time);
        team_id = findViewById(R.id.mark_team_id);
        child_address = findViewById(R.id.mark_children_addres);
        children_image = findViewById(R.id.children_image);


        Intent intent = getIntent();
        child_key = intent.getStringExtra("children_key");
        cnic_father = intent.getStringExtra("father_cnic");
        campaign_key = intent.getStringExtra("campaign_key");


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Campaign").child(campaign_key).child("Children").child(child_key);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {

                    date_and_time = dataSnapshot.child("date_time").getValue().toString();
                    location_latitude = dataSnapshot.child("location_latitude").getValue().toString();
                    location_longitude = dataSnapshot.child("location_longitude").getValue().toString();
                    picture_child = dataSnapshot.child("picture").getValue().toString();
                    team_key = dataSnapshot.child("team_key").getValue().toString();
                    fetch_father_info(date_and_time, picture_child, cnic_father, child_key, team_key);
                } else {
                    pr.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Something was Wrong !!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        map.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                location(location_latitude, location_longitude);
            }
        });

    }

    private void fetch_father_info(final String date_and_time, final String picture_child, final String cnic_father, final String child_key, final String team_key) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Parent").child(cnic_father);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {

                    name_father = dataSnapshot.child("fatherName").getValue().toString();
                    address_child = dataSnapshot.child("address").getValue().toString();

                    fetch_child_info(date_and_time, picture_child, cnic_father, child_key, name_father, address_child, team_key);
                } else {
                    pr.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Something was Wrong !!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void fetch_child_info(final String date_and_time, final String picture_child, final String cnic_father, final String child_key, final String name_father, final String address_child, final String team_key) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Children").child(child_key);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {

                    name_Child = dataSnapshot.child("child_Name").getValue().toString();
                    date_of_birth_child = dataSnapshot.child("dateofBirth").getValue().toString();
                    gender_child = dataSnapshot.child("gender").getValue().toString();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/M/yyyy");
                    mark_date_child = dateFormat.format(new Date(date_and_time));
                    SimpleDateFormat dateFormat1 = new SimpleDateFormat("HH:mm:ss");
                    mark_time_child = dateFormat1.format(new Date(date_and_time));

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(team_key);
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.getValue() != null) {

                                id_team = dataSnapshot.child("id").getValue().toString();

                                Child_name.setText(name_Child);
                                father_name.setText(name_father);
                                father_cnic.setText(cnic_father);
                                child_date_of_birth.setText(date_of_birth_child);
                                child_gender.setText(gender_child);
                                child_mark_date.setText(mark_date_child);
                                child_mark_time.setText(mark_time_child);
                                team_id.setText(id_team);
                                child_address.setText(address_child);
                                Picasso.get().load(picture_child).into(children_image);
                                Log.i("picture_child" , picture_child);

                                pr.setVisibility(View.INVISIBLE);
                            } else {
                                pr.setVisibility(View.INVISIBLE);
                                Toast.makeText(getApplicationContext(), "Something was Wrong !!!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } else {
                    pr.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Something was Wrong !!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void location(String location_latitude, String location_longitude) {

        Log.i("location_latitude", location_latitude);
        Log.i("location_longitude", location_longitude);
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            Intent intent = new Intent(AdminViewResultChildrenVaccination.this, AdminMap.class);
            intent.putExtra("location_latitude", location_latitude);
            intent.putExtra("location_longitude", location_longitude);
            intent.putExtra("Action_map", "Single_show");
            getApplicationContext().startActivity(intent);
        } else {
            on_internet_dailog();
        }
    }

    private void on_internet_dailog() {

        new AlertDialog.Builder(AdminViewResultChildrenVaccination.this)
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
