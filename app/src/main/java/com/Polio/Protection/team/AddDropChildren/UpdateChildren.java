package com.Polio.Protection.team.AddDropChildren;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.Polio.Protection.R;

public class UpdateChildren extends AppCompatActivity {

    Button update_btn, dailog_update_btn, dailog_cancel_btn;
    TextView drop;

    EditText update_children_name, update_father_name, update_father_id_card, update_birth_date;
    private RadioGroup radioGroup;
    DatePickerDialog picker;

    String update_name_children, update_name_father, update_id_card_father, update_date_birth, update_gender_children;

    ImageView backarrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_update_children);

        backarrow = findViewById(R.id.backsign);
        backarrow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        update_children_name = findViewById(R.id.update_children_name);
        update_father_name = findViewById(R.id.update_father_name);
        update_father_id_card = findViewById(R.id.update_ic_card_number);
        update_birth_date = findViewById(R.id.update_date_of_birth);
        radioGroup = findViewById(R.id.update_gender);

        update_btn = findViewById(R.id.update_children_btn);
        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(UpdateChildren.this);
                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.team_add_children_dailog, null);
                alertDialog.getWindow().setContentView(dialogView);

                dailog_update_btn = dialogView.findViewById(R.id.add_btn_dailog);
                dailog_cancel_btn = dialogView.findViewById(R.id.cancel_but_dailog);

                dailog_update_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        
                    }
                });
                dailog_cancel_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

            }
        });

        drop = findViewById(R.id.clear_all);
        drop.setPaintFlags(drop.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

    }
}
