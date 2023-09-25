package com.Polio.Protection.admin.ViewResult;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class AdminViewResultChildren extends AppCompatActivity {

    ImageView backarrow, map;
    String team_key, campaign_key, action;
    String mark_date, mark_time, children_name, father_cnic, children_key, children_total;
    ProgressBar pr;
    TextView total_children;

    int i = 1;
    RecyclerView.Adapter ViewResultAdapter;
    RecyclerView ViewResultRecyclerView;
    RecyclerView.LayoutManager ViewResultLayoutManager;
    final ArrayList<AdminViewResultChildrenItem> adminViewResultChildrenItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_view_result_children);

        map = findViewById(R.id.map);
        backarrow = findViewById(R.id.backsign);
        backarrow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        pr = findViewById(R.id.progressBar);
        total_children = findViewById(R.id.mark_children);

        Intent intent = getIntent();
        team_key = intent.getStringExtra("id_team");
        campaign_key = intent.getStringExtra("campaign_key");
        action = intent.getStringExtra("action");

        if (action.equals("search")) {

            Query query = FirebaseDatabase.getInstance().getReference("Campaign").child(campaign_key).child("Children");
            query.orderByChild("team_key").equalTo(team_key).addListenerForSingleValueEvent(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    children_total = String.valueOf(dataSnapshot.getChildrenCount());
                    total_children.setText(children_total);
                    if (dataSnapshot.getValue() != null) {

                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                            children_key = dataSnapshot1.child("children_key").getValue().toString();
                            children_name = dataSnapshot1.child("children_name").getValue().toString();
                            father_cnic = dataSnapshot1.child("father_cnic").getValue().toString();
                            String date_and_time = dataSnapshot1.child("date_time").getValue().toString();

                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/M/yyyy");
                            mark_date = dateFormat.format(new Date(date_and_time));

                            SimpleDateFormat dateFormat2 = new SimpleDateFormat("HH:mm:ss");
                            mark_time = dateFormat2.format(new Date(date_and_time));

                            adminViewResultChildrenItems.add(new AdminViewResultChildrenItem(children_name, children_key, father_cnic, mark_date, mark_time, campaign_key, i));
                            i++;
                        }
                        ViewResultAdapter = new AdminViewResultChildrenAdapter(getApplicationContext(), adminViewResultChildrenItems);
                        ViewResultRecyclerView.setAdapter(ViewResultAdapter);
                        pr.setVisibility(View.INVISIBLE);

                    } else {
                        pr.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "Not Mark any Children !!!", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
            i = 1;
            ViewResultRecyclerView = findViewById(R.id.recyclerview_children_list_viewresult);
            ViewResultLayoutManager = new LinearLayoutManager(getApplicationContext());
            ViewResultRecyclerView.setLayoutManager(ViewResultLayoutManager);
            ViewResultRecyclerView.setHasFixedSize(true);
            ViewResultRecyclerView.setItemViewCacheSize(20);
            ViewResultRecyclerView.setDrawingCacheEnabled(true);

        } else {

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Campaign").child(campaign_key).child("Children");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    children_total = String.valueOf(dataSnapshot.getChildrenCount());
                    total_children.setText(children_total);
                    if (dataSnapshot.getValue() != null) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                            children_key = dataSnapshot1.child("children_key").getValue().toString();
                            children_name = dataSnapshot1.child("children_name").getValue().toString();
                            father_cnic = dataSnapshot1.child("father_cnic").getValue().toString();
                            String date_and_time = dataSnapshot1.child("date_time").getValue().toString();

                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/M/yyyy");
                            mark_date = dateFormat.format(new Date(date_and_time));

                            SimpleDateFormat dateFormat2 = new SimpleDateFormat("HH:mm:ss");
                            mark_time = dateFormat2.format(new Date(date_and_time));

                            adminViewResultChildrenItems.add(new AdminViewResultChildrenItem(children_name, children_key, father_cnic, mark_date, mark_time, campaign_key, i));
                            i++;
                        }
                        ViewResultAdapter = new AdminViewResultChildrenAdapter(getApplicationContext(), adminViewResultChildrenItems);
                        ViewResultRecyclerView.setAdapter(ViewResultAdapter);
                        pr.setVisibility(View.INVISIBLE);

                    } else {
                        pr.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "Not Mark any Children !!!", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            i = 1;
            ViewResultRecyclerView = findViewById(R.id.recyclerview_children_list_viewresult);
            ViewResultLayoutManager = new LinearLayoutManager(getApplicationContext());
            ViewResultRecyclerView.setLayoutManager(ViewResultLayoutManager);
            ViewResultRecyclerView.setHasFixedSize(true);
            ViewResultRecyclerView.setItemViewCacheSize(20);
            ViewResultRecyclerView.setDrawingCacheEnabled(true);
        }

        map.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                boolean connected = false;
                ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    Intent intent = new Intent(AdminViewResultChildren.this, AdminMap.class);
                    intent.putExtra("Action_map", action);
                    intent.putExtra("team_key", team_key);
                    intent.putExtra("campaign_key", campaign_key);
                    getApplicationContext().startActivity(intent);
                } else {
                    on_internet_dailog();
                }

            }
        });
    }

    private void on_internet_dailog() {

        new AlertDialog.Builder(AdminViewResultChildren.this)
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
