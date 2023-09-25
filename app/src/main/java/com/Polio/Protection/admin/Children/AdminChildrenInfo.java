package com.Polio.Protection.admin.Children;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.Polio.Protection.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminChildrenInfo extends AppCompatActivity {


    ImageView backarrow;
    ProgressBar pr;

    TextView child_name, father_name, father_cnic, child_date_of_birth, child_gender, child_address, child_add_date, child_add_id, child_update_date, child_update_id;
    String name_child, name_father, cnic_father, date_of_birth_child, gender_child, address_child, date_child_add, id_child_add, date_child_update, id_child_update;
    String child_key, temp_one, temp_two;
    TextView vaccination_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_children_info);

        backarrow = findViewById(R.id.backsign);
        backarrow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        pr = findViewById(R.id.progressBar);

        child_name = findViewById(R.id.mark_children_child_name);
        father_name = findViewById(R.id.children_father_name);
        father_cnic = findViewById(R.id.children_father_cnic);
        child_date_of_birth = findViewById(R.id.children_date_of_birth);
        child_gender = findViewById(R.id.children_gender);
        child_address = findViewById(R.id.children_addres);
        child_add_date = findViewById(R.id.children_add_date);
        child_add_id = findViewById(R.id.children_add_id);
        child_update_date = findViewById(R.id.children_update_date);
        child_update_id = findViewById(R.id.children_update_id);

        vaccination_info = findViewById(R.id.vaccination_info);
        vaccination_info.setPaintFlags(vaccination_info.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        Intent intent = getIntent();
        cnic_father = intent.getStringExtra("Father_Cnic");
        child_key = intent.getStringExtra("Children_key");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Parent").child(cnic_father);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {

                    name_father = dataSnapshot.child("fatherName").getValue().toString();
                    address_child = dataSnapshot.child("address").getValue().toString();
                    fetch_children_data(cnic_father, name_father, address_child, child_key);

                } else {
                    pr.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Some Thing was Wrong!!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        vaccination_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                child_vaccination_info(cnic_father, child_key);
            }
        });
    }

    private void fetch_children_data(final String cnic_father, final String name_father, final String address_child, String child_key) {


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Children").child(child_key);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {

                    name_child = dataSnapshot.child("child_Name").getValue().toString();
                    gender_child = dataSnapshot.child("gender").getValue().toString();
                    date_of_birth_child = dataSnapshot.child("dateofBirth").getValue().toString();
                    date_child_add = dataSnapshot.child("child_Add_Date").getValue().toString();
                    id_child_add = dataSnapshot.child("team_key").getValue().toString();
                    date_child_update = dataSnapshot.child("child_update_date").getValue().toString();
                    id_child_update = dataSnapshot.child("team_key_update").getValue().toString();

                    fetch_team_id(cnic_father, name_father, address_child, name_child, gender_child, date_of_birth_child, date_child_add, id_child_add, date_child_update, id_child_update);
                } else {
                    pr.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Some Thing was Wrong!!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void fetch_team_id(final String cnic_father, final String name_father, final String address_child, final String name_child, final String gender_child, final String date_of_birth_child, final String date_child_add, String id_child_add, final String date_child_update, final String id_child_update) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(id_child_add);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    temp_one = dataSnapshot.child("id").getValue().toString();
                    child_add_id.setText(temp_one);

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

        if (id_child_update.equals("null")) {

            child_name.setText(name_child);
            father_name.setText(name_father);
            father_cnic.setText(cnic_father);
            child_date_of_birth.setText(date_of_birth_child);
            child_gender.setText(gender_child);
            child_address.setText(address_child);
            child_add_date.setText(date_child_add);
            child_update_date.setText(date_child_update);
            child_update_id.setText(id_child_update);
            pr.setVisibility(View.INVISIBLE);

        } else {

            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(id_child_update);
            reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getValue() != null) {
                        temp_two = dataSnapshot.child("id").getValue().toString();

                        child_name.setText(name_child);
                        father_name.setText(name_father);
                        father_cnic.setText(cnic_father);
                        child_date_of_birth.setText(date_of_birth_child);
                        child_gender.setText(gender_child);
                        child_address.setText(address_child);
                        child_add_date.setText(date_child_add);
                        child_update_date.setText(date_child_update);
                        child_update_id.setText(temp_two);
                        pr.setVisibility(View.INVISIBLE);
                    } else {

                        child_name.setText(name_child);
                        father_name.setText(name_father);
                        father_cnic.setText(cnic_father);
                        child_date_of_birth.setText(date_of_birth_child);
                        child_gender.setText(gender_child);
                        child_address.setText(address_child);
                        child_add_date.setText(date_child_add);
                        child_update_date.setText(date_child_update);
                        child_update_id.setText(id_child_update);
                        pr.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    pr.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Something was wrong !!!", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void child_vaccination_info(String cnic_father, String child_key) {

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

            Intent intent = new Intent(AdminChildrenInfo.this, AdminChildrenVaccinationInfo.class);
            intent.putExtra("children_key", child_key);
            intent.putExtra("father_cnic", cnic_father);
            this.startActivity(intent);
        } else {
            on_internet_dailog();
        }

    }

    private void on_internet_dailog() {

        new AlertDialog.Builder(AdminChildrenInfo.this)
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
