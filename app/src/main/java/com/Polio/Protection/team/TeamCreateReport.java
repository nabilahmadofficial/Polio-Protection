package com.Polio.Protection.team;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.Polio.Protection.R;


public class TeamCreateReport extends AppCompatActivity {

    ImageView backarrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_create_report);

        backarrow = findViewById(R.id.backsign);
        backarrow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });


    }
}
