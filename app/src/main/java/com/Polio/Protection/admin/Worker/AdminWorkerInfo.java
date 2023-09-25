package com.Polio.Protection.admin.Worker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.Polio.Protection.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminWorkerInfo extends AppCompatActivity {

    TextView worker_id, worker_name, worker_cell_number, worker_team, add_id, add_date, update_id, update_date;
    String id_worker, name_worker, cell_number_worker, team_worker, id_add, date_add, id_update, date_update;
    String temp_key;
    ProgressBar pr;

    String temp_one, temp_two;
    ImageView backarrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_worker_info);


        backarrow = findViewById(R.id.backsign);
        backarrow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        pr = findViewById(R.id.progressBar);

        Intent intent = getIntent();
        temp_key = intent.getStringExtra("worker_key");
        Log.i("Hello", temp_key);

        worker_id = findViewById(R.id.worker_id);
        worker_name = findViewById(R.id.worker_name);
        worker_cell_number = findViewById(R.id.worker_cell_number);
        worker_team = findViewById(R.id.worker_team_id);
        add_id = findViewById(R.id.worker_add_id);
        add_date = findViewById(R.id.worker_add_date);
        update_id = findViewById(R.id.worker_update_id);
        update_date = findViewById(R.id.worker_update_date);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Worker").child(temp_key);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {

                    id_worker = dataSnapshot.child("worker_id").getValue().toString();
                    name_worker = dataSnapshot.child("worker_name").getValue().toString();
                    cell_number_worker = dataSnapshot.child("worker_cell_number").getValue().toString();
                    team_worker = dataSnapshot.child("worker_team_id").getValue().toString();
                    id_add = dataSnapshot.child("worker_add_admin_key").getValue().toString();
                    date_add = dataSnapshot.child("worker_add_date").getValue().toString();
                    id_update = dataSnapshot.child("worker_update_admin_key").getValue().toString();
                    date_update = dataSnapshot.child("worker_update_date").getValue().toString();


                    fetch_id(id_worker, name_worker, team_worker, id_add, date_add, id_update, date_update);
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

    private void fetch_id(final String id_worker, final String name_worker, final String team_worker, String id_add, final String date_add, final String id_update, final String date_update) {

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

        if (this.id_update.equals("null")) {

            worker_id.setText(id_worker);
            worker_name.setText(name_worker);
            worker_cell_number.setText(cell_number_worker);
            worker_team.setText(team_worker);
            add_date.setText(date_add);
            update_id.setText(id_update);
            update_date.setText(date_update);
            pr.setVisibility(View.INVISIBLE);

        } else {

            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(this.id_update);
            reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getValue() != null) {
                        temp_two = dataSnapshot.child("id").getValue().toString();

                        worker_id.setText(id_worker);
                        worker_name.setText(name_worker);
                        worker_cell_number.setText(cell_number_worker);
                        worker_team.setText(team_worker);
                        add_date.setText(date_add);
                        update_id.setText(temp_two);
                        update_date.setText(date_update);
                        pr.setVisibility(View.INVISIBLE);

                    } else {

                        worker_id.setText(id_worker);
                        worker_name.setText(name_worker);
                        worker_cell_number.setText(cell_number_worker);
                        worker_team.setText(team_worker);
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
