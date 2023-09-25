package com.Polio.Protection.admin.Teams;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.Polio.Protection.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminTeams extends AppCompatActivity {


    ImageView backarrow, addsign;
    EditText team_id;
    Button search_team;
    TextView show_all_team;
    String id_team;
    int i = 1;
    ProgressBar pr;

    RecyclerView.Adapter TeamAdapter;
    RecyclerView TeamRecyclerView;
    RecyclerView.LayoutManager TeamLayoutManager;
    final ArrayList<AdminTeamsItem> adminTeamsItemArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_teams);

        backarrow = findViewById(R.id.backsign);
        addsign = findViewById(R.id.addsign);
        team_id = findViewById(R.id.teams_id);
        search_team = findViewById(R.id.search_teams_id_btn);
        show_all_team = findViewById(R.id.show_all_teams);

        backarrow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        pr = findViewById(R.id.progressBar);
        pr.setVisibility(View.INVISIBLE);

        addsign.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(AdminTeams.this, AdminAddUpdateTeams.class);
                intent.putExtra("Action", "Add_Team");
                startActivity(intent);
            }
        });

        search_team.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                id_team = team_id.getText().toString().trim();

                pr.setVisibility(View.VISIBLE);

                if (id_team.isEmpty() || id_team.length() < 6) {
                    pr.setVisibility(View.INVISIBLE);
                    team_id.setError("Team Id cannot be less than 6 characters!");
                    team_id.requestFocus();
                    return;
                } else {
                    boolean connected = false;
                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                        team_search(id_team);
                    } else {
                        pr.setVisibility(View.INVISIBLE);
                        on_internet_dailog();
                    }
                }
            }
        });

        show_all_team.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                boolean connected = false;
                pr.setVisibility(View.VISIBLE);

                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    team_id.getText().clear();
                    team_show_all();
                } else {
                    pr.setVisibility(View.INVISIBLE);
                    on_internet_dailog();
                }
            }
        });

    }

    private void team_search(final String id_team) {
        adminTeamsItemArrayList.clear();
        Query query = FirebaseDatabase.getInstance().getReference("Teams");
        query.orderByChild("id_teams").equalTo(id_team).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        String team_key = dataSnapshot1.child("team_key").getValue().toString();
                        String team_id = dataSnapshot1.child("id_teams").getValue().toString();
                        adminTeamsItemArrayList.add(new AdminTeamsItem(team_key,team_id,i));
                    }
                    TeamAdapter = new AdminTeamsAdapter(getApplicationContext(), adminTeamsItemArrayList);
                    TeamRecyclerView.setAdapter(TeamAdapter);
                    pr.setVisibility(View.INVISIBLE);
                } else {
                    pr.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Team Data Not Found With the ID !!! " + id_team, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        i = 1;
        TeamRecyclerView = findViewById(R.id.recyclerview_teams);
        TeamLayoutManager = new LinearLayoutManager(getApplicationContext());
        TeamRecyclerView.setLayoutManager(TeamLayoutManager);
        TeamRecyclerView.setHasFixedSize(true);
        TeamRecyclerView.setItemViewCacheSize(20);
        TeamRecyclerView.setDrawingCacheEnabled(true);
    }

    private void team_show_all() {

        adminTeamsItemArrayList.clear();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Teams");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        String team_key = dataSnapshot1.child("team_key").getValue().toString();
                        String team_id = dataSnapshot1.child("id_teams").getValue().toString();
                        adminTeamsItemArrayList.add(new AdminTeamsItem(team_key,team_id,i));
                        i++;
                    }
                    TeamAdapter = new AdminTeamsAdapter(getApplicationContext(), adminTeamsItemArrayList);
                    TeamRecyclerView.setAdapter(TeamAdapter);
                    pr.setVisibility(View.INVISIBLE);
                } else {
                    pr.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Teams Data Not Found !!!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        i = 1;
        TeamRecyclerView = findViewById(R.id.recyclerview_teams);
        TeamLayoutManager = new LinearLayoutManager(getApplicationContext());
        TeamRecyclerView.setLayoutManager(TeamLayoutManager);
        TeamRecyclerView.setHasFixedSize(true);
        TeamRecyclerView.setItemViewCacheSize(20);
        TeamRecyclerView.setDrawingCacheEnabled(true);
    }

    private void on_internet_dailog() {

        new AlertDialog.Builder(AdminTeams.this)
                .setTitle("No Internet Connection")
                .setMessage("You need to have Mobile Data or wifi to access this. Press ok")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
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