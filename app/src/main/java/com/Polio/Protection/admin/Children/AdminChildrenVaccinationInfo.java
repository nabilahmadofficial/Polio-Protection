package com.Polio.Protection.admin.Children;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.Polio.Protection.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class AdminChildrenVaccinationInfo extends AppCompatActivity {

    ImageView backarrow;
    ProgressBar pr;
    String child_key, father_cnic;
    int i = 1;

    RecyclerView.Adapter Childrenadapter;
    RecyclerView ChildrenrecyclerView;
    RecyclerView.LayoutManager ChildrenlayoutManager;
    final ArrayList<AdminChildrenVaccinationInfoItem> adminChildrenVaccinationInfoItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_children_vaccination_info);

        backarrow = findViewById(R.id.backsign);
        backarrow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        pr = findViewById(R.id.progressBar);


        Intent intent = getIntent();
        father_cnic = intent.getStringExtra("father_cnic");
        child_key = intent.getStringExtra("children_key");


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Campaign");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        String campaign_key = dataSnapshot1.child("campaign_key").getValue().toString();
                        String campaign_id = dataSnapshot1.child("campaign_id").getValue().toString();
                        check_campaign(child_key, campaign_key, campaign_id);
                    }
                } else {
                    //pr.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Something was Wrong !!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //pr.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Something was wrong !!!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void check_campaign(final String child_key, final String campaign_key, final String campaign_id) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Campaign").child(campaign_key).child("Children").child(child_key);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {

                    String date_and_time = dataSnapshot.child("date_time").getValue().toString();

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/M/yyyy");
                    String mark_date = dateFormat.format(new Date(date_and_time));

                    SimpleDateFormat dateFormat2 = new SimpleDateFormat("HH:mm:ss");
                    String mark_time = dateFormat2.format(new Date(date_and_time));

                    adminChildrenVaccinationInfoItems.add(new AdminChildrenVaccinationInfoItem(campaign_id, mark_date, mark_time, child_key, campaign_key, i));
                    i++;
                }
                Childrenadapter = new AdminChildrenVaccinationInfoAdapter(getApplicationContext(), adminChildrenVaccinationInfoItems);
                ChildrenrecyclerView.setAdapter(Childrenadapter);
                pr.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //pr.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Something was wrong !!!", Toast.LENGTH_SHORT).show();
            }
        });
        i = 1;
        ChildrenrecyclerView = findViewById(R.id.recyclerview_children_vaccination);
        ChildrenlayoutManager = new LinearLayoutManager(getApplicationContext());
        ChildrenrecyclerView.setLayoutManager(ChildrenlayoutManager);
        ChildrenrecyclerView.setHasFixedSize(true);
        ChildrenrecyclerView.setItemViewCacheSize(20);
        ChildrenrecyclerView.setDrawingCacheEnabled(true);
    }
}
