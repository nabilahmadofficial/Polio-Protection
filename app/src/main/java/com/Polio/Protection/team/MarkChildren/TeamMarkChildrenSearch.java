package com.Polio.Protection.team.MarkChildren;

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
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.ArrayList;

public class TeamMarkChildrenSearch extends AppCompatActivity {

    ImageView backarrow;
    ProgressBar pr;

    EditText id_card_num;
    Button search_id_card;
    String children_name, children_key, father_card_num, campaign_key, campaign_id;
    int i = 1;

    RecyclerView.Adapter Childrenadapter;
    RecyclerView ChildrenrecyclerView;
    RecyclerView.LayoutManager ChildrenlayoutManager;
    final ArrayList<TeamMarkChildrenSearchItem> childrenItemArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_mark_children_search);

        backarrow = findViewById(R.id.backsign);
        backarrow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        pr = findViewById(R.id.progressBar);
        pr.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        campaign_key = intent.getStringExtra("campaign_key");
        campaign_id = intent.getStringExtra("campaign_id");
        Log.i("campaign_key", campaign_key);
        Log.i("campaign_id", campaign_id);

        id_card_num = findViewById(R.id.ic_card_number);
        search_id_card = findViewById(R.id.search_id_card_btn);

        search_id_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                pr.setVisibility(View.VISIBLE);

                father_card_num = id_card_num.getText().toString().trim();
                if (father_card_num.isEmpty() || father_card_num.length() < 13) {
                    pr.setVisibility(View.INVISIBLE);
                    id_card_num.setError("Id Card Number cannot be less than 13 characters!");
                    id_card_num.requestFocus();
                    return;
                }

                boolean connected = false;
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    ChlidrenList(father_card_num, campaign_key);
                } else {
                    on_internet_dailog();
                }
            }
        });
    }

    public void ChlidrenList(final String Cnic, final String campaign_key) {
        childrenItemArrayList.clear();

        Query query = FirebaseDatabase.getInstance().getReference("Children");
        query.orderByChild("father_CNIC").equalTo(Cnic).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        children_name = dataSnapshot1.child("child_Name").getValue().toString();
                        children_key = dataSnapshot1.child("child_key").getValue().toString();

                        check_mark(children_key, children_name, campaign_key, campaign_id, Cnic);
                    }
                } else {
                    pr.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "ID Card Number not Found!!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void check_mark(final String children_key, final String children_name, final String campaign_key, final String campaign_id, final String cnic){

        Query query = FirebaseDatabase.getInstance().getReference("Campaign").child(this.campaign_key).child("Children");
        query.orderByChild("children_key").equalTo(this.children_key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    childrenItemArrayList.add(new TeamMarkChildrenSearchItem(children_key, i, children_name, "yes", campaign_key, campaign_id, cnic));
                    i++;
                }
                else {
                    childrenItemArrayList.add(new TeamMarkChildrenSearchItem(children_key, i , children_name, "no", campaign_key, campaign_id, cnic));
                    i++;
                }
                Log.i("dataSnapshot" , String.valueOf(dataSnapshot));
                Childrenadapter = new TeamMarkChildrenSearchAdapter(getApplicationContext(), childrenItemArrayList);
                ChildrenrecyclerView.setAdapter(Childrenadapter);
                pr.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                pr.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        i = 1;
        ChildrenrecyclerView = findViewById(R.id.recyclerview_children_search);
        ChildrenlayoutManager = new LinearLayoutManager(getApplicationContext());
        ChildrenrecyclerView.setLayoutManager(ChildrenlayoutManager);
        ChildrenrecyclerView.setHasFixedSize(true);
        ChildrenrecyclerView.setItemViewCacheSize(20);
        ChildrenrecyclerView.setDrawingCacheEnabled(true);
    }

    private void on_internet_dailog() {

        new AlertDialog.Builder(TeamMarkChildrenSearch.this)
                .setTitle("No Internet Connection")
                .setMessage("You need to have Mobile Data or wifi to access this. Press ok")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
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

