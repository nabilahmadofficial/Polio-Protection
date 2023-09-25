package com.Polio.Protection.admin.ViewResult;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AdminViewResult extends AppCompatActivity {

    ImageView backarrow;
    EditText team_id;
    Button search_team;
    TextView show_all_team;
    String id_team, campaign_key, campaign_id;
    ProgressBar pr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_view_result);

        backarrow = findViewById(R.id.backsign);
        backarrow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        pr = findViewById(R.id.progressBar);
        pr.setVisibility(View.INVISIBLE);

        team_id = findViewById(R.id.teams_id);
        search_team = findViewById(R.id.search_teams_id_btn);
        show_all_team = findViewById(R.id.show_all_teams);


        Intent intent = getIntent();
        campaign_key = intent.getStringExtra("campaign_key");
        campaign_id = intent.getStringExtra("campaign_id");

        search_team.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                pr.setVisibility(View.VISIBLE);
                id_team = team_id.getText().toString().trim();

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
                        pr.setVisibility(View.VISIBLE);
                        team_search(id_team, campaign_key);
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

                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    team_id.getText().clear();
                    //team_show_all();
                    Intent intent = new Intent(AdminViewResult.this, AdminViewResultChildren.class);
                    intent.putExtra("campaign_key", campaign_key);
                    intent.putExtra("action", "show_all");
                    getApplication().startActivity(intent);

                } else {
                    pr.setVisibility(View.INVISIBLE);
                    on_internet_dailog();
                }
            }
        });

    }

    private void team_search(final String id_team, final String campaign_key) {

        Query query = FirebaseDatabase.getInstance().getReference("Users");
        query.orderByChild("id").equalTo(id_team).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        String team_key = dataSnapshot1.getKey();
                        Intent intent = new Intent(AdminViewResult.this, AdminViewResultChildren.class);
                        intent.putExtra("id_team", team_key);
                        intent.putExtra("campaign_key", campaign_key);
                        intent.putExtra("action", "search");
                        getApplication().startActivity(intent);
                        pr.setVisibility(View.INVISIBLE);
                    }
                } else {
                    pr.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Team Not Found With the ID !!! " + id_team, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void on_internet_dailog() {

        new AlertDialog.Builder(AdminViewResult.this)
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
