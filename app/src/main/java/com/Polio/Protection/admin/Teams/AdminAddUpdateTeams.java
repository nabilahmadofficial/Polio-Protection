package com.Polio.Protection.admin.Teams;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.icu.text.SimpleDateFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Polio.Protection.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class AdminAddUpdateTeams extends AppCompatActivity {

    ImageView backarrow, info;
    String temp, temp_key;
    TextView top_text, clear_all_and_delete;
    Button teams_btn;

    ProgressBar pr;

    EditText teams_id, teams_email, teams_password, worker_one_id, worker_two_id;

    String key_team, id_teams, email, password_teams, id_one_worker, id_two_worker, type = "team", id_add, date_add, id_update = "null", date_update = "null";

//    ValueEventListener listener;
//    ArrayAdapter<String> adapter;
//    ArrayList<String> spinnerDataList;
//    DatabaseReference databaseReference;
//    Spinner worker_one_id, worker_two_id;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_teams_add_update);

        info = findViewById(R.id.team_info);
        backarrow = findViewById(R.id.backsign);
        backarrow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        pr = findViewById(R.id.progressBar);
        pr.setVisibility(View.INVISIBLE);

        top_text = findViewById(R.id.top_text);
        clear_all_and_delete = findViewById(R.id.clear_all_and_delete);
        teams_btn = findViewById(R.id.teams_btn);
        clear_all_and_delete.setPaintFlags(clear_all_and_delete.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        teams_id = findViewById(R.id.team_id);
        teams_email = findViewById(R.id.team_email);
        teams_password = findViewById(R.id.team_password);
        worker_one_id = findViewById(R.id.worker_one_id);
        worker_two_id = findViewById(R.id.worker_two_id);

        Intent intent = getIntent();
        temp = intent.getStringExtra("Action");
        temp_key = intent.getStringExtra("teams_key");

        if (temp.equals("Add_Team")) {

            top_text.setText("Add New Team");
            clear_all_and_delete.setText("clear all");
            teams_btn.setText("Add");

            clear_all_and_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pr.setVisibility(View.VISIBLE);
                    clear_all();
                }
            });

            teams_btn.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {

                    boolean connected = false;
                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                        pr.setVisibility(View.VISIBLE);
                        add_team();
                    } else {
                        on_internet_dailog();
                    }

                }
            });

        } else if (temp.equals("Update_Delete_Team")) {

            top_text.setText("Update & Delete Teams");
            // clear_all_and_delete.setText("delete");
            clear_all_and_delete.setVisibility(View.INVISIBLE);
            teams_btn.setText("Update");

            teams_email.setVisibility(View.GONE);
            teams_password.setVisibility(View.GONE);

            info.setVisibility(View.VISIBLE);
            info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AdminAddUpdateTeams.this, AdminTeamsInfo.class);
                    intent.putExtra("team_key", temp_key);
                    startActivity(intent);
                }
            });

            pr.setVisibility(View.VISIBLE);

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Teams").child(temp_key);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getValue() != null) {

                        id_teams = dataSnapshot.child("id_teams").getValue().toString();
                        id_one_worker = dataSnapshot.child("id_one_worker").getValue().toString();
                        id_two_worker = dataSnapshot.child("id_two_worker").getValue().toString();

                        teams_id.setText(id_teams);
                        teams_id.setEnabled(false);
                        worker_one_id.setText(id_one_worker);
                        worker_two_id.setText(id_two_worker);

                        pr.setVisibility(View.INVISIBLE);
                    } else {
                        pr.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "Worker Data Not Found !!!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


//            clear_all_and_delete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    boolean connected = false;
//                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//                    if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
//                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
//                        delete_team(temp_key);
//                    } else {
//                        on_internet_dailog();
//                    }
//                }
//            });

            teams_btn.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {
                    boolean connected = false;
                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                        pr.setVisibility(View.VISIBLE);
                        update_team(temp_key, id_teams, id_one_worker, id_two_worker);
                    } else {
                        on_internet_dailog();
                    }
                }
            });

        } else {
            Toast.makeText(getApplicationContext(), "SomeThing Wrong Try Again !!!", Toast.LENGTH_LONG).show();
        }

//        databaseReference = FirebaseDatabase.getInstance().getReference("Worker");
//        worker_one_id = (Spinner) findViewById(R.id.worker_one_spinner);
//        worker_two_id = (Spinner) findViewById(R.id.worker_two_spinner);
//        spinnerDataList = new ArrayList<>();
//        adapter = new ArrayAdapter<String>(AdminAddUpdateTeams.this, android.R.layout.simple_spinner_dropdown_item, spinnerDataList);
//        worker_one_id.setAdapter(adapter);
//        worker_two_id.setAdapter(adapter);
//        spiner_data();
    }

    private void clear_all() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        teams_email.getText().clear();
        teams_password.getText().clear();
        teams_id.getText().clear();
        worker_one_id.getText().clear();
        worker_two_id.getText().clear();

        pr.setVisibility(View.INVISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void add_team() {

        email = teams_email.getText().toString().trim();
        password_teams = teams_password.getText().toString().trim();
        id_teams = teams_id.getText().toString().trim();
        id_one_worker = worker_one_id.getText().toString().trim();
        id_two_worker = worker_two_id.getText().toString().trim();

        if (email.isEmpty()) {
            pr.setVisibility(View.INVISIBLE);
            teams_email.setError("Please write Email");
            teams_email.requestFocus();
            return;
        }
        if (password_teams.isEmpty()) {
            pr.setVisibility(View.INVISIBLE);
            teams_password.setError("Please write Password");
            teams_password.requestFocus();
            return;
        }
        if (id_teams.isEmpty() || id_teams.length() < 6) {
            pr.setVisibility(View.INVISIBLE);
            teams_id.setError("Team ID cannot be less than 6 characters!");
            teams_id.requestFocus();
            return;
        }
        if (id_one_worker.isEmpty() || id_one_worker.length() < 8) {
            pr.setVisibility(View.INVISIBLE);
            worker_one_id.setError("Worker ID cannot be less than 8 characters!");
            worker_one_id.requestFocus();
            return;
        }
        if (id_two_worker.isEmpty() || id_two_worker.length() < 8) {
            pr.setVisibility(View.INVISIBLE);
            worker_two_id.setError("Worker ID cannot be less than 8 characters!");
            worker_two_id.requestFocus();
            return;
        }
        if (id_one_worker.equals(id_two_worker)) {
            pr.setVisibility(View.INVISIBLE);
            worker_one_id.setError("Both worker ID same!");
            worker_one_id.requestFocus();
            worker_two_id.setError("Both worker ID same!");
            worker_two_id.requestFocus();
            return;
        }

        id_add = FirebaseAuth.getInstance().getCurrentUser().getUid();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        date_add = dateFormat.format(new Date());

        Query query = FirebaseDatabase.getInstance().getReference("Teams");
        query.orderByChild("id_teams").equalTo(id_teams).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() == null) {

                    check_worker_team(id_one_worker, id_two_worker, id_add, date_add, id_teams, email, password_teams);

                } else {
                    pr.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Team Data Already Exist With the ID !!! " + id_teams, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

//    private void delete_team(String temp_key) {
//
//    }

    private void update_team(final String team_temp_key, final String id_teams, final String id_one_worker, final String id_two_worker) {

        final String temp_one = worker_one_id.getText().toString().trim();
        final String temp_two = worker_two_id.getText().toString().trim();

        if (temp_one.isEmpty() || temp_one.length() < 8) {
            pr.setVisibility(View.INVISIBLE);
            worker_one_id.setError("Worker ID cannot be less than 8 characters!");
            worker_one_id.requestFocus();
            return;
        }
        if (temp_two.isEmpty() || temp_two.length() < 8) {
            pr.setVisibility(View.INVISIBLE);
            worker_two_id.setError("Worker ID cannot be less than 8 characters!");
            worker_two_id.requestFocus();
            return;
        }

        pr.setVisibility(View.INVISIBLE);
        Log.i("id_one_worker", id_one_worker);
        Log.i("id_two_worker", id_two_worker);
        Log.i("temp_one", temp_one);
        Log.i("temp_two", temp_two);

        if (temp_one.equals(id_one_worker)) {
            Toast.makeText(getApplicationContext(), "Yes", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "No", Toast.LENGTH_LONG).show();

            Query query = FirebaseDatabase.getInstance().getReference("Worker");
            query.orderByChild("worker_id").equalTo(temp_one).addListenerForSingleValueEvent(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getValue() != null) {

                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                            final String temp_team_id_one = dataSnapshot1.child("worker_team_id").getValue().toString();

                            if (temp_team_id_one.equals("null")) {

                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                                String temp_date = dateFormat.format(new Date());
                                String temp_key = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                dataSnapshot1.getRef().child("worker_team_id").setValue(id_teams);
                                dataSnapshot1.getRef().child("worker_update_date").setValue(temp_date);
                                dataSnapshot1.getRef().child("worker_update_admin_key").setValue(temp_key);

                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                databaseReference.child("Teams").child(team_temp_key).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if (dataSnapshot.getValue() != null) {
                                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                                            String temp_date = dateFormat.format(new Date());
                                            String temp_key = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                            dataSnapshot.getRef().child("date_update").setValue(temp_date);
                                            dataSnapshot.getRef().child("id_update").setValue(temp_key);
                                            dataSnapshot.getRef().child("id_one_worker").setValue(temp_one);

                                        } else {
                                            pr.setVisibility(View.INVISIBLE);
                                            Toast.makeText(getApplicationContext(), "Something Wrong !!!", Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });


                            } else {
                                pr.setVisibility(View.INVISIBLE);
                                Toast.makeText(getApplicationContext(), "Worker one Already Add other Team !!! ID " + temp_team_id_one, Toast.LENGTH_LONG).show();
                            }
                        }

                    } else {
                        pr.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "Worker one Data Not Exist With the ID !!! " + temp_one, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

            Query query1 = FirebaseDatabase.getInstance().getReference("Worker");
            query1.orderByChild("worker_id").equalTo(id_one_worker).addListenerForSingleValueEvent(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getValue() != null) {

                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                            String temp_date = dateFormat.format(new Date());
                            String temp_key = FirebaseAuth.getInstance().getCurrentUser().getUid();

                            dataSnapshot1.getRef().child("worker_team_id").setValue("null");
                            dataSnapshot1.getRef().child("worker_update_date").setValue(temp_date);
                            dataSnapshot1.getRef().child("worker_update_admin_key").setValue(temp_key);
                        }

                    } else {
                        pr.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "Something wrong !!!", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });


        }

        if (temp_two.equals(id_two_worker)) {
            Toast.makeText(getApplicationContext(), "Yes", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "No", Toast.LENGTH_LONG).show();

            Query query = FirebaseDatabase.getInstance().getReference("Worker");
            query.orderByChild("worker_id").equalTo(temp_two).addListenerForSingleValueEvent(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getValue() != null) {

                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            final String temp_team_id_two = dataSnapshot1.child("worker_team_id").getValue().toString();

                            if (temp_team_id_two.equals("null")) {

                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                                String temp_date = dateFormat.format(new Date());
                                String temp_key = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                dataSnapshot1.getRef().child("worker_team_id").setValue(id_teams);
                                dataSnapshot1.getRef().child("worker_update_date").setValue(temp_date);
                                dataSnapshot1.getRef().child("worker_update_admin_key").setValue(temp_key);


                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                databaseReference.child("Teams").child(team_temp_key).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if (dataSnapshot.getValue() != null) {
                                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                                            String temp_date = dateFormat.format(new Date());
                                            String temp_key = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                            dataSnapshot.getRef().child("date_update").setValue(temp_date);
                                            dataSnapshot.getRef().child("id_update").setValue(temp_key);
                                            dataSnapshot.getRef().child("id_two_worker").setValue(temp_two);

                                        } else {
                                            pr.setVisibility(View.INVISIBLE);
                                            Toast.makeText(getApplicationContext(), "Something Wrong !!!", Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });


                            } else {
                                pr.setVisibility(View.INVISIBLE);
                                Toast.makeText(getApplicationContext(), "Worker two Already Add other Team !!! ID " + temp_team_id_two, Toast.LENGTH_LONG).show();
                            }
                        }

                    } else {
                        pr.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "Worker two Data Not Exist With the ID !!! " + temp_two, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

            Query query1 = FirebaseDatabase.getInstance().getReference("Worker");
            query1.orderByChild("worker_id").equalTo(id_two_worker).addListenerForSingleValueEvent(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getValue() != null) {

                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                            String temp_date = dateFormat.format(new Date());
                            String temp_key = FirebaseAuth.getInstance().getCurrentUser().getUid();

                            dataSnapshot1.getRef().child("worker_team_id").setValue("null");
                            dataSnapshot1.getRef().child("worker_update_date").setValue(temp_date);
                            dataSnapshot1.getRef().child("worker_update_admin_key").setValue(temp_key);
                        }

                    } else {
                        pr.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "Something wrong !!!", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }

    private void check_worker_team(final String id_one_worker, final String id_two_worker, final String id_add, final String date_add, final String id_teams, final String email_teams, final String password_teams) {

        Query query = FirebaseDatabase.getInstance().getReference("Worker");
        query.orderByChild("worker_id").equalTo(id_one_worker).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        String temp_team_id_one = dataSnapshot1.child("worker_team_id").getValue().toString();
                        final String temp_worker_key_one = dataSnapshot1.child("worker_key").getValue().toString();

                        if (temp_team_id_one.equals("null")) {

                            Query query = FirebaseDatabase.getInstance().getReference("Worker");
                            query.orderByChild("worker_id").equalTo(id_two_worker).addListenerForSingleValueEvent(new ValueEventListener() {
                                @RequiresApi(api = Build.VERSION_CODES.N)
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.getValue() != null) {

                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                            final String temp_team_id_two = dataSnapshot1.child("worker_team_id").getValue().toString();
                                            final String temp_worker_key_two = dataSnapshot1.child("worker_key").getValue().toString();

                                            if (temp_team_id_two.equals("null")) {

                                                add_login(temp_worker_key_one, temp_worker_key_two, id_one_worker, id_two_worker, id_add, date_add, id_update, date_update, id_teams, email_teams, password_teams, type);

                                            } else {
                                                pr.setVisibility(View.INVISIBLE);
                                                Toast.makeText(getApplicationContext(), "Worker two Already Add other Team !!! ID " + temp_team_id_two, Toast.LENGTH_LONG).show();
                                            }
                                        }

                                    } else {
                                        pr.setVisibility(View.INVISIBLE);
                                        Toast.makeText(getApplicationContext(), "Worker two Data Not Exist With the ID !!! " + AdminAddUpdateTeams.this.id_one_worker, Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });

                        } else {
                            pr.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Worker one Already Add other Team !!! ID " + temp_team_id_one, Toast.LENGTH_LONG).show();
                        }
                    }

                } else {
                    pr.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Worker one Data Not Exist With the ID !!! " + AdminAddUpdateTeams.this.id_one_worker, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void add_login(final String temp_worker_key_one, final String temp_worker_key_two, final String id_one_worker, final String id_two_worker, final String id_add, final String date_add, final String id_update, final String date_update, final String id_teams, final String email, String password_teams, final String type) {

        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password_teams)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            AdminTeamsLogin adminTeamsLogin = new AdminTeamsLogin(

                                    id_teams, email, type
                            );
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(adminTeamsLogin).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        team_add_ok(temp_worker_key_one, temp_worker_key_two, id_one_worker, id_two_worker, id_add, date_add, id_update, date_update, id_teams);

                                    } else {
                                        //display a failure message
                                        pr.setVisibility(View.INVISIBLE);
                                        Toast.makeText(AdminAddUpdateTeams.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            pr.setVisibility(View.INVISIBLE);
                            Toast.makeText(AdminAddUpdateTeams.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void team_add_ok(final String temp_worker_key_one, final String temp_worker_key_two, String id_one_worker, String id_two_worker, String id_add, String date_add, String id_update, String date_update, final String id_teams) {

        DatabaseReference wDatabaseRef = FirebaseDatabase.getInstance().getReference("Teams");
        DatabaseReference newChildRef = wDatabaseRef.push();
        key_team = newChildRef.getKey();

        AdminTeamsModal adminTeamsModal = new AdminTeamsModal(

                key_team, id_teams, id_one_worker, id_two_worker, id_add, date_add, id_update, date_update
        );

        wDatabaseRef.child(key_team).setValue(adminTeamsModal).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    update_worker_team(temp_worker_key_one, temp_worker_key_two, id_teams);
                } else {
                    pr.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void update_worker_team(final String temp_worker_key_one, final String temp_worker_key_two, final String id_teams) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Worker").child(temp_worker_key_one).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                dataSnapshot.getRef().child("worker_team_id").setValue(id_teams);

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("Worker").child(temp_worker_key_two).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        dataSnapshot.getRef().child("worker_team_id").setValue(id_teams);
                        clear_all();
                        pr.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "Team Data Add Successfully", Toast.LENGTH_LONG).show();
                        //mAuth.signOut();
                        finish();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        pr.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                pr.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void on_internet_dailog() {

        new AlertDialog.Builder(AdminAddUpdateTeams.this)
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

//    public void spiner_data() {
//        listener = databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                Log.i("AABB", String.valueOf(dataSnapshot));
//                for (DataSnapshot item : dataSnapshot.getChildren()) {
//                    String start_date = item.child("worker_id").getValue().toString();
//                    Log.i("AACC", String.valueOf(start_date));
//                    spinnerDataList.add(start_date);
//                    Log.i("AADD", String.valueOf(spinnerDataList));
//                }
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
}
