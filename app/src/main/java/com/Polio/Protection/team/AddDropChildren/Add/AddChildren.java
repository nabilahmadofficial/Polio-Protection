package com.Polio.Protection.team.AddDropChildren.Add;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.icu.text.SimpleDateFormat;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.Polio.Protection.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class AddChildren extends AppCompatActivity{

    Button add_btn, dailog_add_btn, dailog_cancel_btn;
    TextView all_clear;
    TextView dailog_name, dailog_father_name, dailog_father_cnic, dailog_date_of_birth, dailog_gender, dailog_address;

    protected LocationManager locationManager;
    protected LocationListener locationListener;
    DatePickerDialog picker;
    EditText children_name, father_name, father_id_card, birth_date;
    private RadioGroup radioGroup;

    String Child_key, Child_Name, DateofBirth, Gender, Team_key, Location_latitude, Location_longitude, Child_Add_Date, Team_key_update, Child_update_date;
    String Fatherkey, FatherName, FatherCnic , Address;

    ImageView backarrow;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_add_children);

        backarrow = findViewById(R.id.backsign);
        backarrow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        children_name = findViewById(R.id.add_children_name);
        father_name = findViewById(R.id.add_father_name);
        father_id_card = findViewById(R.id.ic_card_number);
        birth_date = findViewById(R.id.date_of_birth);
        radioGroup = findViewById(R.id.gender);


        birth_date.setInputType(InputType.TYPE_NULL);
        birth_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(AddChildren.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                birth_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        radioGroup.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group,int checkedId)
                    {
                        RadioButton radioButton = group.findViewById(checkedId);
                    }
                });

        add_btn = findViewById(R.id.add_children_btn);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {


                SimpleDateFormat dateFormat  = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String date  = dateFormat .format(new Date());

//                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                DatabaseReference dbRef = database.getReference().child("/Parent/3510387991971/Children");
//                DatabaseReference newChildRef = dbRef.push();
//                String key = newChildRef.getKey();

                //dbRef.child(key).setValue();

//                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                DatabaseReference dbRef = database.getReference().child("Children").child("3510387991971");



//
//                FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
//                DatabaseReference mDatabaseReference = mDatabase.getReference();
//                mDatabaseReference = mDatabase.getReference().child("Children");
//                mDatabaseReference.setValue(childrenAdd);


//                int selectedId = radioGroup.getCheckedRadioButtonId();
//                if (selectedId == -1) {
//                    Toast.makeText(AddChildren.this, "Please selete gender", Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    RadioButton radioButton = radioGroup.findViewById(selectedId);
//
//                    ChildName = children_name.getText().toString().trim();
//                    FatherName = father_name.getText().toString().trim();
//                    FatherCnic = father_id_card.getText().toString().trim();
//                    DateofBirth = birth_date.getText().toString().trim();
//                    Gender = radioButton.getText().toString().trim();
//
//                    if (ChildName.isEmpty()) {
//                        children_name.setError("Please write children name");
//                        children_name.requestFocus();
//                        return;
//                    }
//                    if (FatherName.isEmpty()) {
//                        father_name.setError("Please write father name");
//                        father_name.requestFocus();
//                        return;
//                    }
//                    if (FatherCnic.isEmpty()) {
//                        father_id_card.setError("Please write father name");
//                        father_id_card.requestFocus();
//                        return;
//                    }
//                    if (Gender.isEmpty()) {
//                        birth_date.setError("Please write date of birth name");
//                        birth_date.requestFocus();
//                        return;
//                    }
//                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AddChildren.this);
//                final AlertDialog alertDialog = dialogBuilder.create();
//                alertDialog.show();
//                LayoutInflater inflater = getLayoutInflater();
//                View dialogView = inflater.inflate(R.layout.team_add_children_dailog, null);
//                alertDialog.getWindow().setContentView(dialogView);
//
////                    if( alertDialog != null && alertDialog.isShowing())
////                    {
////                        Toast.makeText(getApplicationContext(), "Hello", Toast.LENGTH_LONG).show();
////
////                    }
//
//                    dailog_add_btn = dialogView.findViewById(R.id.add_btn_dailog);
//                    dailog_cancel_btn = dialogView.findViewById(R.id.cancel_but_dailog);
//
//                    dailog_name = dialogView.findViewById(R.id.dailog_child_name);
//                    dailog_father_name = dialogView.findViewById(R.id.dailog_father_name);
//                    dailog_father_cnic = dialogView.findViewById(R.id.dailog_father_cnic);
//                    dailog_date_of_birth = dialogView.findViewById(R.id.dailog_date_of_birth);
//                    dailog_gender = dialogView.findViewById(R.id.dailog_gender_cnic);
//                    dailog_address = dialogView.findViewById(R.id.dailog_addres_text);
//
//                    dailog_name.setText(ChildName);
//                    dailog_father_name.setText(FatherName);
//                    dailog_father_cnic.setText(FatherCnic);
//                    dailog_date_of_birth.setText(DateofBirth);
//                    dailog_gender.setText(Gender);
//
//                    dailog_add_btn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                        }
//                    });
//                    dailog_cancel_btn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            alertDialog.dismiss();
//                        }
//                    });
//                }
            }
        });

        all_clear = findViewById(R.id.clear_all);
        all_clear.setPaintFlags(all_clear.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        all_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                children_name.getText().clear();
                father_name.getText().clear();
                father_id_card.getText().clear();
                birth_date.getText().clear();
                radioGroup.clearCheck();
            }
        });

    }
}