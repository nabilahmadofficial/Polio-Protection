package com.Polio.Protection.team.CheckReport;

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
import android.widget.TextView;
import android.widget.Toast;

import com.Polio.Protection.R;
import com.Polio.Protection.team.Map.TeamMap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class TeamCheckReport extends AppCompatActivity {

    String campaign_key, team_key, mark_date, mark_time, total_children, mark_today;
    ImageView backarrow, map;
    int i = 1;

    TextView today_mark, children_total;
    ProgressBar pr;

    RecyclerView.Adapter CheckReportAdapter;
    RecyclerView CheckReportRecyclerView;
    RecyclerView.LayoutManager CheckReportLayoutManager;
    final ArrayList<TeamCheckReportItem> teamCheckReportItemArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_check_report);

        map = findViewById(R.id.map);
        backarrow = findViewById(R.id.backsign);
        backarrow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        today_mark = findViewById(R.id.today_mark);
        children_total = findViewById(R.id.total_mark);

        pr = findViewById(R.id.progressBar);
        pr.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        campaign_key = intent.getStringExtra("campaign_key");
        team_key = FirebaseAuth.getInstance().getCurrentUser().getUid();

        map.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(TeamCheckReport.this, TeamMap.class);
                intent.putExtra("campaign_key", campaign_key);
                intent.putExtra("team_key", team_key);
                intent.putExtra("Action_map", "all_show");
                getApplicationContext().startActivity(intent);
            }
        });

        Query query = FirebaseDatabase.getInstance().getReference("Campaign").child(campaign_key).child("Children");
        query.orderByChild("team_key").equalTo(team_key).addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.i("dataSnapshot1", String.valueOf(dataSnapshot));
                total_children = String.valueOf(dataSnapshot.getChildrenCount());
                children_total.setText(total_children);
                int j = 0;
                if (dataSnapshot.getValue() != null) {

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {


                        String children_key = dataSnapshot1.child("children_key").getValue().toString();
                        String children_name = dataSnapshot1.child("children_name").getValue().toString();
                        String father_Cnic = dataSnapshot1.child("father_cnic").getValue().toString();
                        String date_and_time = dataSnapshot1.child("date_time").getValue().toString();

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/M/yyyy");
                        mark_date = dateFormat.format(new Date(date_and_time));

                        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd/M/yyyy");
                        String get_date = dateFormat1.format(new Date());

                        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
                        try {
                            if (sdf.parse(mark_date).equals(sdf.parse(get_date))) {
                                j++;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        SimpleDateFormat dateFormat2 = new SimpleDateFormat("HH:mm:ss");
                        mark_time = dateFormat2.format(new Date(date_and_time));

                        teamCheckReportItemArrayList.add(new TeamCheckReportItem(i, children_name, children_key, father_Cnic, mark_date, mark_time, campaign_key));
                    i++;
                    }
                    CheckReportAdapter = new TeamCheckReportAdapter(getApplicationContext(), teamCheckReportItemArrayList);
                    CheckReportRecyclerView.setAdapter(CheckReportAdapter);
                    today_mark.setText(String.valueOf(j));
                    pr.setVisibility(View.INVISIBLE);
                } else {
                    pr.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Not Mark any Children !!!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        i = 1;
        CheckReportRecyclerView = findViewById(R.id.recyclerview_children_list_check_result);
        CheckReportLayoutManager = new LinearLayoutManager(getApplicationContext());
        CheckReportRecyclerView.setLayoutManager(CheckReportLayoutManager);
        CheckReportRecyclerView.setHasFixedSize(true);
        CheckReportRecyclerView.setItemViewCacheSize(20);
        CheckReportRecyclerView.setDrawingCacheEnabled(true);
    }
}
