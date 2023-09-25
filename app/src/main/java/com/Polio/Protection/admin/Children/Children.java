package com.Polio.Protection.admin.Children;

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
import com.Polio.Protection.team.MarkChildren.TeamMarkChildrenSearch;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Children extends AppCompatActivity {

    ImageView backarrow;
    ProgressBar pr;

    EditText id_card_num;
    Button search_id_card;
    TextView show_all;
    String father_card_num, children_name, children_key;
    int i = 1;

    RecyclerView.Adapter Childrenadapter;
    RecyclerView ChildrenrecyclerView;
    RecyclerView.LayoutManager ChildrenlayoutManager;
    final ArrayList<ChildrenItem> childrenItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_children);

        backarrow = findViewById(R.id.backsign);
        backarrow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        pr = findViewById(R.id.progressBar);
        pr.setVisibility(View.INVISIBLE);

        id_card_num = findViewById(R.id.ic_card_number);
        search_id_card = findViewById(R.id.search_id_card_btn);
        show_all = findViewById(R.id.show_all_children);


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
                    ChlidrenList(father_card_num);
                } else {
                    on_internet_dailog();
                }
            }
        });

        show_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                pr.setVisibility(View.VISIBLE);

                boolean connected = false;
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    id_card_num.getText().clear();
                    show_all();
                } else {
                    on_internet_dailog();
                }
            }
        });

    }

    private void show_all() {

        childrenItems.clear();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Children");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        children_name = dataSnapshot1.child("child_Name").getValue().toString();
                        children_key = dataSnapshot1.child("child_key").getValue().toString();
                        String CNIC = dataSnapshot1.child("father_CNIC").getValue().toString();
                        childrenItems.add(new ChildrenItem(children_key, children_name, CNIC, i));
                        i++;
                    }
                } else {
                    pr.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Campaign Data Not Found !!!", Toast.LENGTH_SHORT).show();
                }
                Childrenadapter = new ChildrenAdapter(getApplicationContext(), childrenItems);
                ChildrenrecyclerView.setAdapter(Childrenadapter);
                pr.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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

    private void ChlidrenList(final String father_card_num) {

        childrenItems.clear();

        Query query = FirebaseDatabase.getInstance().getReference("Children");
        query.orderByChild("father_CNIC").equalTo(father_card_num).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        children_name = dataSnapshot1.child("child_Name").getValue().toString();
                        children_key = dataSnapshot1.child("child_key").getValue().toString();
                        childrenItems.add(new ChildrenItem(children_key, children_name, father_card_num, i));
                        i++;
                    }
                } else {
                    pr.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "ID Card Number not Found!!!", Toast.LENGTH_SHORT).show();
                }
                Childrenadapter = new ChildrenAdapter(getApplicationContext(), childrenItems);
                ChildrenrecyclerView.setAdapter(Childrenadapter);
                pr.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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

        new AlertDialog.Builder(Children.this)
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
