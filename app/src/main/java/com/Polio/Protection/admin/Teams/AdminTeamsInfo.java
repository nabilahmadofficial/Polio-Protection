package com.Polio.Protection.admin.Teams;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AdminTeamsInfo extends AppCompatActivity {

    String temp_key;
    ProgressBar pr;
    ImageView backarrow;

    TextView team_email, team_id, worker_one_id, worker_two_id, add_id, add_date, update_id, update_date;
    String email_team, id_team, id_worker_one, id_worker_two, id_add, date_add, id_update, date_update;

    String temp_one, temp_two;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_teams_info);

        backarrow = findViewById(R.id.backsign);
        backarrow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        pr = findViewById(R.id.progressBar);

        Intent intent = getIntent();
        temp_key = intent.getStringExtra("team_key");

        team_email = findViewById(R.id.team_email);
        team_id = findViewById(R.id.team_id);
        worker_one_id = findViewById(R.id.worker_one_id);
        worker_two_id = findViewById(R.id.worker_two_id);
        add_id = findViewById(R.id.team_add_id);
        add_date = findViewById(R.id.team_add_date);
        update_id = findViewById(R.id.team_update_id);
        update_date = findViewById(R.id.team_update_date);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Teams").child(temp_key);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {

                    id_team = dataSnapshot.child("id_teams").getValue().toString();
                    id_worker_one = dataSnapshot.child("id_one_worker").getValue().toString();
                    id_worker_two = dataSnapshot.child("id_two_worker").getValue().toString();
                    id_add = dataSnapshot.child("id_add").getValue().toString();
                    date_add = dataSnapshot.child("date_add").getValue().toString();
                    id_update = dataSnapshot.child("id_update").getValue().toString();
                    date_update = dataSnapshot.child("date_update").getValue().toString();

                    fetch_id(id_team, id_worker_one, id_worker_two, id_add, date_add, id_update, date_update);

                } else {
                    pr.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Team Data Not Found !!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pr.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Something was wrong !!!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void fetch_id(final String id_team, final String id_worker_one, final String id_worker_two, String id_add, final String date_add, final String id_update, final String date_update) {

        Query query = FirebaseDatabase.getInstance().getReference("Users");
        query.orderByChild("id").equalTo(id_team).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    Log.i("dataSnapshot", String.valueOf(dataSnapshot));

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        email_team = dataSnapshot1.child("email").getValue().toString();
                        team_email.setText(email_team);
                    }
                } else {
                    pr.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Something was wrong !!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                pr.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Something was wrong !!!", Toast.LENGTH_SHORT).show();
            }
        });


        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(id_add);
        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
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

        if (this.id_update.equals("null")) {

            team_id.setText(id_team);
            worker_one_id.setText(id_worker_one);
            worker_two_id.setText(id_worker_two);
            add_date.setText(date_add);
            update_id.setText(id_update);
            update_date.setText(date_update);
            pr.setVisibility(View.INVISIBLE);

        } else {

            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Users").child(id_update);
            reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getValue() != null) {
                        temp_two = dataSnapshot.child("id").getValue().toString();

                        team_id.setText(id_team);
                        worker_one_id.setText(id_worker_one);
                        worker_two_id.setText(id_worker_two);
                        add_date.setText(date_add);
                        update_id.setText(temp_two);
                        update_date.setText(date_update);
                        pr.setVisibility(View.INVISIBLE);

                    } else {

                        team_id.setText(id_team);
                        worker_one_id.setText(id_worker_one);
                        worker_two_id.setText(id_worker_two);
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
