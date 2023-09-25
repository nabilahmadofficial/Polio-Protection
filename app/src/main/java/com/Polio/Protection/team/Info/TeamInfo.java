package com.Polio.Protection.team.Info;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.Polio.Protection.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class TeamInfo extends AppCompatActivity {

    ImageView backarrow;

    TextView team_id, team_email, id_one, name_one, id_two, name_two;
    String id_team, email_team, one_id, one_name, two_id, two_name;

    ProgressBar pr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_info);

        backarrow = findViewById(R.id.backsign);
        backarrow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        pr = findViewById(R.id.progressBar);

        team_id = findViewById(R.id.info_team_id);
        team_email = findViewById(R.id.info_team_email);
        id_one = findViewById(R.id.info_worker_one_id);
        name_one = findViewById(R.id.info_worker_one_name);
        id_two = findViewById(R.id.info_worker_two_id);
        name_two = findViewById(R.id.info_worker_two_name);


        String team_key = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(team_key);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {

                    id_team = dataSnapshot.child("id").getValue().toString();
                    email_team = dataSnapshot.child("email").getValue().toString();

                    fetch_worker_id(id_team, email_team);
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

    private void fetch_worker_id(final String id_team, final String email_team) {

        Query query = FirebaseDatabase.getInstance().getReference("Teams");
        query.orderByChild("id_teams").equalTo(id_team).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        one_id = dataSnapshot1.child("id_one_worker").getValue().toString();
                        two_id = dataSnapshot1.child("id_two_worker").getValue().toString();
                        fetch_worker_name(id_team, email_team, one_id, two_id);
                    }
                    Log.i("dataSnapshot", String.valueOf(dataSnapshot));

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

    private void fetch_worker_name(final String id_team, final String email_team, final String one_id, final String two_id) {

        Query query = FirebaseDatabase.getInstance().getReference("Worker");
        query.orderByChild("worker_id").equalTo(one_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        one_name = dataSnapshot1.child("worker_name").getValue().toString();
                    }

                    Query query = FirebaseDatabase.getInstance().getReference("Worker");
                    query.orderByChild("worker_id").equalTo(two_id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (dataSnapshot.getValue() != null) {

                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    two_name = dataSnapshot1.child("worker_name").getValue().toString();
                                }
                                disply_data(id_team, email_team, one_id, two_id, one_name, two_name);

                            } else {
                                pr.setVisibility(View.INVISIBLE);
                                Toast.makeText(getApplicationContext(), "Some Thing was Wrong !!!", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                } else {
                    pr.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Some Thing was Wrong !!!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    private void disply_data(String id_team, String email_team, String one_id, String two_id, String one_name, String two_name) {

        team_id.setText(id_team);
        team_email.setText(email_team);
        id_one.setText(one_id);
        name_one.setText(one_name);
        id_two.setText(two_id);
        name_two.setText(two_name);
        pr.setVisibility(View.INVISIBLE);
    }
}
