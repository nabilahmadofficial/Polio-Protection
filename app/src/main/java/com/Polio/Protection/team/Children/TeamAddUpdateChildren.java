package com.Polio.Protection.team.Children;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.icu.text.SimpleDateFormat;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.v4.os.ResultReceiver;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.Polio.Protection.R;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TeamAddUpdateChildren extends Activity {

    Button add_update_btn;
    TextView all_clear_delete, top_text;

    DatePickerDialog picker;
    EditText children_name, father_name, father_id_card, birth_date;
    private RadioGroup radioGroup;

    String Child_self_key, Child_Name, DateofBirth, Gender, Team_key, Child_add_Date, Location_latitude, Location_longitude, Team_key_update = "null", Child_update_date = "null";
    String Father_key, Father_Name, Father_Cnic = "", Address;

    String temp, temp_key, father_cnic;
    ImageView backarrow;
    private FirebaseAuth mAuth;

    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    Geocoder geocoder;
    List<Address> addressList;

    ProgressBar pr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_children_add_update);


        backarrow = findViewById(R.id.backsign);
        backarrow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        pr = findViewById(R.id.progressBar);
        pr.setVisibility(View.INVISIBLE);

        top_text = findViewById(R.id.top_text);
        all_clear_delete = findViewById(R.id.clear_all_delete);
        add_update_btn = findViewById(R.id.add_update_children_btn);
        all_clear_delete.setPaintFlags(all_clear_delete.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        children_name = findViewById(R.id.add_children_name);
        father_name = findViewById(R.id.add_father_name);
        father_id_card = findViewById(R.id.id_card_number);
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
                picker = new DatePickerDialog(TeamAddUpdateChildren.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                birth_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        Intent intent = getIntent();
        temp = intent.getStringExtra("Action");
        father_cnic = intent.getStringExtra("Father_cnic");
        temp_key = intent.getStringExtra("children_key");
//        Log.i("temp_key", temp_key);

        if (temp.equals("Add_Children")) {

            top_text.setText("Add New Children");
            all_clear_delete.setText("clear all");
            add_update_btn.setText("Add");

            all_clear_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pr.setVisibility(View.VISIBLE);
                    clear_data();
                }
            });

            add_update_btn.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {

//                    boolean connected = false;
//                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//                    if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
//                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
//                        pr.setVisibility(View.VISIBLE);
//                        add_children();
//                    } else {
//                        on_internet_dailog();
//                    }

                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(TeamAddUpdateChildren.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
                    } else {

                        boolean connected = false;
                        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

                            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                OnGPS();
                            } else {
                                pr.setVisibility(View.VISIBLE);
                                getCurrentLocation();
                            }
                        } else {
                            on_internet_dailog();
                        }
                    }

                }
            });

        } else if (temp.equals("Update_Delete_Children")) {

            top_text.setText("Update & Delete Children");
            //all_clear_delete.setText("delete");
            add_update_btn.setText("Update");
            pr.setVisibility(View.VISIBLE);


            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Parent").child(father_cnic);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getValue() != null) {

                        String name_father = dataSnapshot.child("fatherName").getValue().toString();
                        father_name.setText(name_father);
                        father_id_card.setText(father_cnic);

                        father_name.setEnabled(false);
                        father_id_card.setEnabled(false);

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Children").child(temp_key);
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.getValue() != null) {

                                    String name_children = dataSnapshot.child("child_Name").getValue().toString();
                                    String date_of_birth_children = dataSnapshot.child("dateofBirth").getValue().toString();
                                    String children_gender = dataSnapshot.child("gender").getValue().toString();

                                    children_name.setText(name_children);
                                    birth_date.setText(date_of_birth_children);

                                    if (children_gender.equals("Male")) {
                                        ((RadioButton) radioGroup.getChildAt(0)).setChecked(true);

                                    } else if (children_gender.equals("Female")) {
                                        ((RadioButton) radioGroup.getChildAt(1)).setChecked(true);
                                    }

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
                    } else {
                        pr.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "Children Data Not Found !!!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


//            all_clear_delete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    boolean connected = false;
//                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//                    if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
//                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
//                        //delete_children(father_cnic, temp_key);
//                    } else {
//                        on_internet_dailog();
//                    }
//                }
//            });

            add_update_btn.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {
                    boolean connected = false;
                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                        pr.setVisibility(View.VISIBLE);
                        update_children(father_cnic, temp_key);
                    } else {
                        on_internet_dailog();
                    }
                }
            });

        } else {
            Toast.makeText(getApplicationContext(), "SomeThing Wrong Try Again !!!", Toast.LENGTH_LONG).show();
        }

    }

    private void getCurrentLocation() {

        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(TeamAddUpdateChildren.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {

                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(TeamAddUpdateChildren.this)
                                .removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int latestLocationIndex = locationResult.getLocations().size() - 1;

                            double latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                            double longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();

                            Location_latitude = String.valueOf(latitude);
                            Location_longitude = String.valueOf(longitude);
                            getAddress(latitude, longitude);
                        }
                    }
                }, Looper.getMainLooper());

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getAddress(double latitude, double longitude) {
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addressList = geocoder.getFromLocation(latitude, longitude, 1);

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addressList == null || addressList.isEmpty()) {

        } else {
            android.location.Address address = addressList.get(0);
            ArrayList<String> addressFragments = new ArrayList<>();
            for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }
            Address = String.valueOf(addressFragments);
            add_children();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void add_children() {

        radioGroup.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        RadioButton radioButton = group.findViewById(checkedId);
                    }
                });

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Child_add_Date = dateFormat.format(new Date());

        int selectedId = radioGroup.getCheckedRadioButtonId();

        Child_Name = children_name.getText().toString().trim();
        Father_Name = father_name.getText().toString().trim();
        Father_Cnic = father_id_card.getText().toString().trim();
        DateofBirth = birth_date.getText().toString().trim();

        if (Child_Name.isEmpty()) {
            pr.setVisibility(View.INVISIBLE);
            children_name.setError("Please write children name");
            children_name.requestFocus();
            return;
        }
        if (Father_Name.isEmpty()) {
            pr.setVisibility(View.INVISIBLE);
            father_name.setError("Please write father name");
            father_name.requestFocus();
            return;
        }
        if (Father_Cnic.isEmpty() || Father_Cnic.length() < 13) {
            pr.setVisibility(View.INVISIBLE);
            father_id_card.setError("Id Card Number cannot be less than 13 characters!");
            father_id_card.requestFocus();
            return;
        }
        if (DateofBirth.isEmpty()) {
            pr.setVisibility(View.INVISIBLE);
            birth_date.setError("Please Enter Date of Birth");
            birth_date.requestFocus();
            return;
        }

        if (selectedId == -1) {
            pr.setVisibility(View.INVISIBLE);
            Toast.makeText(TeamAddUpdateChildren.this, "Please selete gender", Toast.LENGTH_SHORT).show();
        } else {
            RadioButton radioButton = radioGroup.findViewById(selectedId);
            Gender = radioButton.getText().toString().trim();

            final String temp_name_one;
            temp_name_one = Child_Name.replaceAll(" ", "").toUpperCase();
            Child_self_key = Father_Cnic + temp_name_one;

            DatabaseReference fDatabaseRef = FirebaseDatabase.getInstance().getReference("Parent").child(Father_Cnic);
            fDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {

                    if (snapshot.getValue() == null) {

                        add_parent_data(Child_Name, Child_self_key, Father_Name, Father_Cnic, DateofBirth, Gender, Address);

                    } else {

                        Query query = FirebaseDatabase.getInstance().getReference("Children");
                        query.orderByChild("child_self_key").equalTo(Child_self_key).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (dataSnapshot.getValue() == null) {

                                    add_children_data(Child_Name, Child_self_key, Father_Cnic, DateofBirth, Gender);
                                } else {
                                    pr.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getApplicationContext(), "Children Already Exist !!!", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void add_parent_data(final String child_name, final String child_self_key, String father_Name, final String father_Cnic, final String dateofBirth, final String gender, String address) {

        DatabaseReference cDatabaseRef = FirebaseDatabase.getInstance().getReference("Parent");
        DatabaseReference newChildRef = cDatabaseRef.push();
        String key = newChildRef.getKey();

        TeamFatherModal teamFatherModal = new TeamFatherModal(
                key, father_Name, father_Cnic, address
        );

        cDatabaseRef.child(father_Cnic).setValue(teamFatherModal).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    add_children_data(child_name, child_self_key, father_Cnic, dateofBirth, gender);

                } else {
                    pr.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void add_children_data(String child_name, String child_self_key, String father_Cnic, String dateofBirth, String gender) {

        DatabaseReference cDatabaseRef = FirebaseDatabase.getInstance().getReference("Children");
        DatabaseReference newChildRef = cDatabaseRef.push();
        String key = newChildRef.getKey();
        Team_key = FirebaseAuth.getInstance().getCurrentUser().getUid();

        TeamChildrenModal teamChildrenModal = new TeamChildrenModal(
                key, Child_self_key, child_name, father_Cnic, dateofBirth, gender, Team_key, Child_add_Date, Location_latitude, Location_longitude, Team_key_update, Child_update_date
        );

        cDatabaseRef.child(key).setValue(teamChildrenModal).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    Toast.makeText(getApplicationContext(), "Children Data Add Successfully !!!", Toast.LENGTH_LONG).show();
                    clear_data();
                    finish();
                } else {
                    pr.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void update_children(final String father_cnic, final String temp_key) {

        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        radioGroup.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        RadioButton radioButton = group.findViewById(checkedId);
                    }
                });

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Child_update_date = dateFormat.format(new Date());
        int selectedId = radioGroup.getCheckedRadioButtonId();

        Child_Name = children_name.getText().toString().trim();
        DateofBirth = birth_date.getText().toString().trim();

        if (Child_Name.isEmpty()) {
            pr.setVisibility(View.INVISIBLE);
            children_name.setError("Please write children name");
            children_name.requestFocus();
            return;
        }
        if (DateofBirth.isEmpty()) {
            pr.setVisibility(View.INVISIBLE);
            birth_date.setError("Please Enter Date of Birth");
            birth_date.requestFocus();
            return;
        }

        if (selectedId == -1) {
            pr.setVisibility(View.INVISIBLE);
            Toast.makeText(TeamAddUpdateChildren.this, "Please selete gender", Toast.LENGTH_SHORT).show();
        } else {
            RadioButton radioButton = radioGroup.findViewById(selectedId);
            Gender = radioButton.getText().toString().trim();
            Team_key_update = FirebaseAuth.getInstance().getCurrentUser().getUid();

            final String temp1, temp2;
            temp1 = Child_Name.replaceAll(" ", "").toUpperCase();
            temp2 = father_cnic + temp1;

            Query query = FirebaseDatabase.getInstance().getReference("Children");
            query.orderByChild("child_self_key").equalTo(temp2).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getValue() == null) {

                        new AlertDialog.Builder(TeamAddUpdateChildren.this)
                                .setTitle("Update Children Data")
                                .setMessage("Are you want to Update Children data. Press yes")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                        databaseReference.child("Children").child(temp_key).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                dataSnapshot.getRef().child("child_Name").setValue(Child_Name);
                                                dataSnapshot.getRef().child("dateofBirth").setValue(DateofBirth);
                                                dataSnapshot.getRef().child("gender").setValue(Gender);
                                                dataSnapshot.getRef().child("team_key_update").setValue(Team_key_update);
                                                dataSnapshot.getRef().child("child_update_date").setValue(Child_update_date);
                                                pr.setVisibility(View.INVISIBLE);
                                                Toast.makeText(getApplicationContext(), "Children Data Update Successfully", Toast.LENGTH_LONG).show();
                                                finish();
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                pr.setVisibility(View.INVISIBLE);
                                                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                pr.setVisibility(View.INVISIBLE);
                                dialog.cancel();
                            }
                        }).show();

                    } else {
                        pr.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "Children Already Exist !!!", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }

    }

    void clear_data() {
        children_name.getText().clear();
        father_name.getText().clear();
        father_id_card.getText().clear();
        birth_date.getText().clear();
        radioGroup.clearCheck();
        pr.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(getApplicationContext(), "Permissions Reqiured", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void on_internet_dailog() {

        new AlertDialog.Builder(TeamAddUpdateChildren.this)
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

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}


