package com.Polio.Protection.admin.ViewResult;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
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
import java.util.ArrayList;

public class AdminViewResultSelectCampaign extends AppCompatActivity {

    ImageView backarrow;
    ProgressBar pr;
    int i = 1;

    String team_add_date, campaign_end_date, id_team;
    String campaign_start_date, campaign_id, campaign_key;

    RecyclerView.Adapter CampaignAdapter;
    RecyclerView CampaignRecyclerView;
    RecyclerView.LayoutManager CampaignLayoutManager;
    final ArrayList<AdminViewResultSelectCampaignItem> adminViewResultSelectCampaignItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_view_result_select_campaign);

        backarrow = findViewById(R.id.backsign);
        backarrow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        pr = findViewById(R.id.progressBar);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Campaign");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        campaign_id = dataSnapshot1.child("campaign_id").getValue().toString();
                        campaign_key = dataSnapshot1.child("campaign_key").getValue().toString();
                        campaign_start_date = dataSnapshot1.child("start_date").getValue().toString();
                        campaign_end_date = dataSnapshot1.child("end_date").getValue().toString();
                        adminViewResultSelectCampaignItems.add(new AdminViewResultSelectCampaignItem(campaign_key,campaign_id,campaign_start_date,campaign_end_date,i));
                        i++;
                    }
                    CampaignAdapter = new AdminViewResultSelectCampaignAdapter(getApplicationContext(), adminViewResultSelectCampaignItems);
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
        CampaignRecyclerView = findViewById(R.id.recyclerview_children_list_result);
        CampaignLayoutManager = new LinearLayoutManager(getApplicationContext());
        CampaignRecyclerView.setLayoutManager(CampaignLayoutManager);
        CampaignRecyclerView.setHasFixedSize(true);
        CampaignRecyclerView.setItemViewCacheSize(20);
        CampaignRecyclerView.setDrawingCacheEnabled(true);

    }
}
