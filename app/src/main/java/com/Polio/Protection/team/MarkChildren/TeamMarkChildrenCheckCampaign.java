package com.Polio.Protection.team.MarkChildren;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.Polio.Protection.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class TeamMarkChildrenCheckCampaign extends AppCompatActivity {

    ImageView backarrow;
    ProgressBar pr;
    int i = 1;
    RecyclerView.Adapter CampaignAdapter;
    RecyclerView CampaignRecyclerView;
    RecyclerView.LayoutManager CampaignLayoutManager;
    final ArrayList<TeamMarkChildrenCheckCampaignItem> teamMarkChildrenCheckCampaignItemArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_mark_children_check_campaign);

        backarrow = findViewById(R.id.backsign);
        backarrow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        pr = findViewById(R.id.progressBar);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Campaign");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/M/yyyy");
                        String get_date = dateFormat.format(new Date());

                        String start_date = dataSnapshot1.child("start_date").getValue().toString();
                        String end_date = dataSnapshot1.child("end_date").getValue().toString();

                        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
                        try {
                            if ((sdf.parse(start_date).before(sdf.parse(get_date)) || sdf.parse(start_date).equals(sdf.parse(get_date)))
                                    && (sdf.parse(end_date).after(sdf.parse(get_date)) || sdf.parse(end_date).equals(sdf.parse(get_date)))) {

                                String campaign_key = dataSnapshot1.child("campaign_key").getValue().toString();
                                String campaign_id = dataSnapshot1.child("campaign_id").getValue().toString();
                                String campaign_star_date = dataSnapshot1.child("start_date").getValue().toString();
                                String campaign_end_date = dataSnapshot1.child("end_date").getValue().toString();
                                teamMarkChildrenCheckCampaignItemArrayList.add(new TeamMarkChildrenCheckCampaignItem(campaign_key, i, campaign_id, campaign_star_date, campaign_end_date));
                                i++;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    CampaignAdapter = new TeamMarkChildrenCheckCampaignAdapter(getApplicationContext(), teamMarkChildrenCheckCampaignItemArrayList);
                    CampaignRecyclerView.setAdapter(CampaignAdapter);
                    pr.setVisibility(View.INVISIBLE);
                    if(teamMarkChildrenCheckCampaignItemArrayList.isEmpty()) {
                        pr.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "No Campaign Active !!!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    pr.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "No Campaign Active !!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        i = 1;
        CampaignRecyclerView = findViewById(R.id.team_mark_children_campaign);
        CampaignLayoutManager = new LinearLayoutManager(getApplicationContext());
        CampaignRecyclerView.setLayoutManager(CampaignLayoutManager);
        CampaignRecyclerView.setHasFixedSize(true);
        CampaignRecyclerView.setItemViewCacheSize(20);
        CampaignRecyclerView.setDrawingCacheEnabled(true);
    }
}
