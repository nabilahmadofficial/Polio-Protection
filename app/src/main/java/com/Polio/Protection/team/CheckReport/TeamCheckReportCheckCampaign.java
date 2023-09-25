package com.Polio.Protection.team.CheckReport;

import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Polio.Protection.R;
import com.Polio.Protection.team.MarkChildren.TeamMarkChildrenCheckCampaignAdapter;
import com.Polio.Protection.team.MarkChildren.TeamMarkChildrenCheckCampaignItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class TeamCheckReportCheckCampaign extends AppCompatActivity {

    ImageView backarrow;
    ProgressBar pr;
    int i = 1;

    String team_add_date, campaign_end_date, id_team;
    String campaign_start_date, campaign_id, campaign_key;

    RecyclerView.Adapter CampaignAdapter;
    RecyclerView CampaignRecyclerView;
    RecyclerView.LayoutManager CampaignLayoutManager;
    final ArrayList<TeamCheckReportCheckCampaignItem> teamCheckReportCheckCampaignItemArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_check_report_check_campaign);

        backarrow = findViewById(R.id.backsign);
        backarrow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        pr = findViewById(R.id.progressBar);

        String team_key = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(team_key);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    id_team = dataSnapshot.child("id").getValue().toString();
                    date_fetch(id_team);
                } else {
                    pr.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Some Thing was Wrong !!!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                pr.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void date_fetch(String id_team) {

        Query query = FirebaseDatabase.getInstance().getReference("Teams");
        query.orderByChild("id_teams").equalTo(id_team).addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        team_add_date = dataSnapshot1.child("date_add").getValue().toString();
                        display_campaign(team_add_date);
                    }
                } else {
                    pr.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Some Thing was Wrong !!!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                pr.setVisibility(View.INVISIBLE);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void display_campaign(final String team_add_date) {

        final SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Campaign");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Log.i("hayyy", String.valueOf(dataSnapshot.getChildrenCount()));
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        campaign_id = dataSnapshot1.child("campaign_id").getValue().toString();
                        campaign_key = dataSnapshot1.child("campaign_key").getValue().toString();
                        campaign_start_date = dataSnapshot1.child("start_date").getValue().toString();
                        campaign_end_date = dataSnapshot1.child("end_date").getValue().toString();
                        try {
                            if ((sdf.parse(campaign_end_date).after(sdf.parse(team_add_date)) || sdf.parse(campaign_end_date).equals(sdf.parse(team_add_date)))) {
                                teamCheckReportCheckCampaignItemArrayList.add(new TeamCheckReportCheckCampaignItem(campaign_key, i, campaign_id, campaign_start_date, campaign_end_date));
                                i++;
                            } else {
                                Toast.makeText(getApplicationContext(), "No", Toast.LENGTH_SHORT).show();
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    CampaignAdapter = new TeamCheckReportCheckCampaignAdapter(getApplicationContext(), teamCheckReportCheckCampaignItemArrayList);
                    CampaignRecyclerView.setAdapter(CampaignAdapter);
                    pr.setVisibility(View.INVISIBLE);
                } else {
                    pr.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Some Thing was Wrong !!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        i = 1;
        CampaignRecyclerView = findViewById(R.id.team_check_report_campaign);
        CampaignLayoutManager = new LinearLayoutManager(getApplicationContext());
        CampaignRecyclerView.setLayoutManager(CampaignLayoutManager);
        CampaignRecyclerView.setHasFixedSize(true);
        CampaignRecyclerView.setItemViewCacheSize(20);
        CampaignRecyclerView.setDrawingCacheEnabled(true);
    }
}
