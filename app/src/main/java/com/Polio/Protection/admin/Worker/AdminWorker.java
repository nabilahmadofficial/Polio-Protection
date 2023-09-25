package com.Polio.Protection.admin.Worker;

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

public class AdminWorker extends AppCompatActivity {

    ImageView backarrow, addsign;
    EditText worker_id;
    Button search_worker;
    TextView show_all_worker;
    String id_worker;
    int i = 1;
    ProgressBar pr;

    RecyclerView.Adapter WorkerAdapter;
    RecyclerView WorkerRecyclerView;
    RecyclerView.LayoutManager WorkerLayoutManager;
    final ArrayList<AdminWorkerItem> adminWorkerItemArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_worker);

        backarrow = findViewById(R.id.backsign);
        addsign = findViewById(R.id.addsign);
        worker_id = findViewById(R.id.worker_id_number);
        search_worker = findViewById(R.id.search_worker_btn);
        show_all_worker = findViewById(R.id.show_all_worker);

        backarrow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        pr = findViewById(R.id.progressBar);
        adminWorkerItemArrayList.clear();

        pr.setVisibility(View.INVISIBLE);

        addsign.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(AdminWorker.this, AdminAddUpdateWorker.class);
                intent.putExtra("Action", "Add_Worker");
                startActivity(intent);
            }
        });

        search_worker.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                id_worker = worker_id.getText().toString().trim();

                pr.setVisibility(View.VISIBLE);

                if (id_worker.isEmpty() || id_worker.length() < 8) {
                    pr.setVisibility(View.INVISIBLE);
                    worker_id.setError("Worker Id cannot be less than 8 characters!");
                    worker_id.requestFocus();
                    return;
                } else {
                    boolean connected = false;
                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                        worker_search(id_worker);
                    } else {
                        pr.setVisibility(View.INVISIBLE);
                        on_internet_dailog();
                    }
                }
            }
        });

        show_all_worker.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                boolean connected = false;
                pr.setVisibility(View.VISIBLE);

                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    worker_id.getText().clear();
                    worker_show_all();
                } else {
                    pr.setVisibility(View.INVISIBLE);
                    on_internet_dailog();
                }
            }
        });
    }

    private void worker_search(final String id) {

        adminWorkerItemArrayList.clear();
        Query query = FirebaseDatabase.getInstance().getReference("Worker");
        query.orderByChild("worker_id").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        String worker_key = dataSnapshot1.child("worker_key").getValue().toString();
                        String worker_name = dataSnapshot1.child("worker_name").getValue().toString();
                        String worker_gender = dataSnapshot1.child("worker_gender").getValue().toString();
                        adminWorkerItemArrayList.add(new AdminWorkerItem(worker_key, worker_name, worker_gender, i));
                        pr.setVisibility(View.INVISIBLE);
                    }
                    WorkerAdapter = new AdminWorkerAdapter(getApplicationContext(), adminWorkerItemArrayList);
                    WorkerRecyclerView.setAdapter(WorkerAdapter);
                } else {
                    pr.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Worker Data Not Found With the ID !!! " + id, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        i = 1;
        WorkerRecyclerView = findViewById(R.id.recyclerview_worker);
        WorkerLayoutManager = new LinearLayoutManager(getApplicationContext());
        WorkerRecyclerView.setLayoutManager(WorkerLayoutManager);
        WorkerRecyclerView.setHasFixedSize(true);
        WorkerRecyclerView.setItemViewCacheSize(20);
        WorkerRecyclerView.setDrawingCacheEnabled(true);
    }

    private void worker_show_all() {

        adminWorkerItemArrayList.clear();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Worker");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        String worker_key = dataSnapshot1.child("worker_key").getValue().toString();
                        String worker_name = dataSnapshot1.child("worker_name").getValue().toString();
                        String worker_gender = dataSnapshot1.child("worker_gender").getValue().toString();
                        adminWorkerItemArrayList.add(new AdminWorkerItem(worker_key, worker_name, worker_gender, i));
                        i++;
                    }
                    WorkerAdapter = new AdminWorkerAdapter(getApplicationContext(), adminWorkerItemArrayList);
                    WorkerRecyclerView.setAdapter(WorkerAdapter);
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
        i = 1;
        WorkerRecyclerView = findViewById(R.id.recyclerview_worker);
        WorkerLayoutManager = new LinearLayoutManager(getApplicationContext());
        WorkerRecyclerView.setLayoutManager(WorkerLayoutManager);
        WorkerRecyclerView.setHasFixedSize(true);
        WorkerRecyclerView.setItemViewCacheSize(20);
        WorkerRecyclerView.setDrawingCacheEnabled(true);
    }

    private void on_internet_dailog() {

        new AlertDialog.Builder(AdminWorker.this)
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
