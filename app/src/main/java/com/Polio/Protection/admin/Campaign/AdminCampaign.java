package com.Polio.Protection.admin.Campaign;

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

public class AdminCampaign extends AppCompatActivity {

    ImageView backarrow, addsign;
    EditText campaign_id;
    Button search_campaign;
    TextView show_all_campaign;
    String id_campaign;
    int i = 1;
    ProgressBar pr;

    RecyclerView.Adapter CampaignAdapter;
    RecyclerView CampaignRecyclerView;
    RecyclerView.LayoutManager CampaignLayoutManager;
    final ArrayList<AdminCampaignItem> adminCampaignItemArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_campaign);

        adminCampaignItemArrayList.clear();

        backarrow = findViewById(R.id.backsign);
        addsign = findViewById(R.id.addsign);
        campaign_id = findViewById(R.id.campaign_id);
        search_campaign = findViewById(R.id.search_campaign_id_btn);
        show_all_campaign = findViewById(R.id.show_all_campaign);

        backarrow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        pr = findViewById(R.id.progressBar);
        pr.setVisibility(View.INVISIBLE);

        addsign.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(AdminCampaign.this, AdminAddUpdateCampaign.class);
                intent.putExtra("Action", "Add_Campaign");
                startActivity(intent);
            }
        });

        search_campaign.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                id_campaign = campaign_id.getText().toString().trim();

                pr.setVisibility(View.VISIBLE);

                if (id_campaign.isEmpty() || id_campaign.length() < 7) {
                    pr.setVisibility(View.INVISIBLE);
                    campaign_id.setError("Campaign Id cannot be less than 7 characters!");
                    campaign_id.requestFocus();
                    return;
                } else {
                    boolean connected = false;
                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                        campaign_search(id_campaign);
                    } else {
                        pr.setVisibility(View.INVISIBLE);
                        on_internet_dailog();
                    }
                }
            }
        });

        show_all_campaign.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                boolean connected = false;
                pr.setVisibility(View.VISIBLE);

                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    campaign_id.getText().clear();
                    campaign_show_all();
                } else {
                    pr.setVisibility(View.INVISIBLE);
                    on_internet_dailog();
                }
            }
        });

    }

    private void campaign_search(final String id_campaign) {

        adminCampaignItemArrayList.clear();
        Query query = FirebaseDatabase.getInstance().getReference("Campaign");
        query.orderByChild("campaign_id").equalTo(id_campaign).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        String campaign_key = dataSnapshot1.child("campaign_key").getValue().toString();
                        String campaign_star_date = dataSnapshot1.child("start_date").getValue().toString();
                        String campaign_end_date = dataSnapshot1.child("end_date").getValue().toString();
                        adminCampaignItemArrayList.add(new AdminCampaignItem(campaign_key, campaign_star_date, campaign_end_date, i));
                        pr.setVisibility(View.INVISIBLE);
                    }
                    CampaignAdapter = new AdminCampaignAdapter(getApplicationContext(), adminCampaignItemArrayList);
                    CampaignRecyclerView.setAdapter(CampaignAdapter);
                } else {
                    pr.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Campaign Data Not Found With the ID !!! " + id_campaign, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        i = 1;
        CampaignRecyclerView = findViewById(R.id.recyclerview_campaign);
        CampaignLayoutManager = new LinearLayoutManager(getApplicationContext());
        CampaignRecyclerView.setLayoutManager(CampaignLayoutManager);
        CampaignRecyclerView.setHasFixedSize(true);
        CampaignRecyclerView.setItemViewCacheSize(20);
        CampaignRecyclerView.setDrawingCacheEnabled(true);
    }

    private void campaign_show_all() {

        adminCampaignItemArrayList.clear();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Campaign");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        String campaign_key = dataSnapshot1.child("campaign_key").getValue().toString();
                        String campaign_star_date = dataSnapshot1.child("start_date").getValue().toString();
                        String campaign_end_date = dataSnapshot1.child("end_date").getValue().toString();
                        adminCampaignItemArrayList.add(new AdminCampaignItem(campaign_key, campaign_star_date, campaign_end_date, i));
                        i++;
                    }
                    CampaignAdapter = new AdminCampaignAdapter(getApplicationContext(), adminCampaignItemArrayList);
                    CampaignRecyclerView.setAdapter(CampaignAdapter);
                    pr.setVisibility(View.INVISIBLE);
                } else {
                    pr.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Campaign Data Not Found !!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        i = 1;
        CampaignRecyclerView = findViewById(R.id.recyclerview_campaign);
        CampaignLayoutManager = new LinearLayoutManager(getApplicationContext());
        CampaignRecyclerView.setLayoutManager(CampaignLayoutManager);
        CampaignRecyclerView.setHasFixedSize(true);
        CampaignRecyclerView.setItemViewCacheSize(20);
        CampaignRecyclerView.setDrawingCacheEnabled(true);
    }

    private void on_internet_dailog() {

        new AlertDialog.Builder(AdminCampaign.this)
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
