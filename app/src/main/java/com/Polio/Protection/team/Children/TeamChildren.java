package com.Polio.Protection.team.Children;

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

public class TeamChildren extends AppCompatActivity {

    ImageView backarrow, addsign;
    EditText father_id_card;
    Button search_children;
    String id_card_children;
    int i = 1;
    ProgressBar pr;

    RecyclerView.Adapter ChildrenAdapter;
    RecyclerView ChildrenRecyclerView;
    RecyclerView.LayoutManager ChildrenLayoutManager;
    final ArrayList<TeamChildrenItem> teamChildrenItemArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_children);

        backarrow = findViewById(R.id.backsign);
        addsign = findViewById(R.id.addsign);
        father_id_card = findViewById(R.id.father_id_card);
        search_children = findViewById(R.id.search_father_id_card_btn);

        backarrow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        pr = findViewById(R.id.progressBar);
        teamChildrenItemArrayList.clear();
        pr.setVisibility(View.INVISIBLE);

        addsign.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(TeamChildren.this, TeamAddUpdateChildren.class);
                intent.putExtra("Action", "Add_Children");
                startActivity(intent);
            }
        });

        search_children.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                id_card_children = father_id_card.getText().toString().trim();

                pr.setVisibility(View.VISIBLE);

                if (id_card_children.isEmpty() || id_card_children.length() < 13) {
                    pr.setVisibility(View.INVISIBLE);
                    father_id_card.setError("Id Card Number cannot be less than 13 characters!");
                    father_id_card.requestFocus();
                    return;
                } else {
                    boolean connected = false;
                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                        children_search(id_card_children);
                    } else {
                        pr.setVisibility(View.INVISIBLE);
                        on_internet_dailog();
                    }
                }
            }
        });
    }

    private void children_search(final String id_card_children) {

        teamChildrenItemArrayList.clear();

        Query query = FirebaseDatabase.getInstance().getReference("Children");
        query.orderByChild("father_CNIC").equalTo(id_card_children).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        String children_key = dataSnapshot1.child("child_key").getValue().toString();
                        String children_name = dataSnapshot1.child("child_Name").getValue().toString();
                        String children_gender = dataSnapshot1.child("gender").getValue().toString();
                        teamChildrenItemArrayList.add(new TeamChildrenItem(children_key, children_name, children_gender, id_card_children, i));
                        i++;
                    }
                    ChildrenAdapter = new TeamChildrenAdapter(getApplicationContext(), teamChildrenItemArrayList);
                    ChildrenRecyclerView.setAdapter(ChildrenAdapter);
                    pr.setVisibility(View.INVISIBLE);
                } else {
                    pr.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Children Data Not Found !!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        i = 1;
        ChildrenRecyclerView = findViewById(R.id.recyclerview_children);
        ChildrenLayoutManager = new LinearLayoutManager(getApplicationContext());
        ChildrenRecyclerView.setLayoutManager(ChildrenLayoutManager);
        ChildrenRecyclerView.setHasFixedSize(true);
        ChildrenRecyclerView.setItemViewCacheSize(20);
        ChildrenRecyclerView.setDrawingCacheEnabled(true);
    }

    private void on_internet_dailog() {

        new AlertDialog.Builder(TeamChildren.this)
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
