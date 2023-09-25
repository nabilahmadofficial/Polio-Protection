package com.Polio.Protection.admin.Campaign;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.google.firebase.database.ValueEventListener;

public class AdminCampaignInfo extends AppCompatActivity {

    String id_team, id_add, date_add, id_update, date_update;
    TextView team_id, add_id, add_date, update_id, update_date;
    String temp_key;
    ProgressBar pr;
    String temp_one, temp_two;

    ImageView backarrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_campaign_info);

        backarrow = findViewById(R.id.backsign);
        backarrow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        pr = findViewById(R.id.progressBar);

        Intent intent = getIntent();
        temp_key = intent.getStringExtra("campaign_key");

        team_id = findViewById(R.id.campaign_id);
        add_id = findViewById(R.id.campaign_add_id);
        add_date = findViewById(R.id.campaign_add_date);
        update_id = findViewById(R.id.campaign_update_id);
        update_date = findViewById(R.id.campaign_update_date);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Campaign").child(temp_key);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {

                    id_team = dataSnapshot.child("campaign_id").getValue().toString();
                    id_add = dataSnapshot.child("admin_key").getValue().toString();
                    date_add = dataSnapshot.child("add_date").getValue().toString();
                    id_update = dataSnapshot.child("update_admin_key").getValue().toString();
                    date_update = dataSnapshot.child("update_date").getValue().toString();

                    fetch_id(id_team, id_add, date_add, id_update, date_update);

                } else {
                    pr.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Worker Data Not Found !!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pr.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Something was wrong !!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetch_id(final String id_team, String id_add, final String date_add, final String id_update, final String date_update) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(id_add);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    temp_one = dataSnapshot.child("id").getValue().toString();
                    add_id.setText(temp_one);

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

        if (id_update.equals("null")) {

            team_id.setText(id_team);
            add_date.setText(date_add);
            update_id.setText(id_update);
            update_date.setText(date_update);
            pr.setVisibility(View.INVISIBLE);

        } else {

            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(id_update);
            reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getValue() != null) {
                        temp_two = dataSnapshot.child("id").getValue().toString();

                        team_id.setText(id_team);
                        add_date.setText(date_add);
                        update_id.setText(temp_two);
                        update_date.setText(date_update);

                        pr.setVisibility(View.INVISIBLE);
                    } else {

                        team_id.setText(id_team);
                        add_date.setText(date_add);
                        update_id.setText(id_update);
                        update_date.setText(date_update);
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
}
