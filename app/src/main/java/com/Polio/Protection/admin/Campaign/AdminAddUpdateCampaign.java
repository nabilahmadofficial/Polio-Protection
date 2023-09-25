package com.Polio.Protection.admin.Campaign;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
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
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.Polio.Protection.R;
import com.Polio.Protection.admin.Worker.AdminAddUpdateWorker;
import com.Polio.Protection.admin.Worker.AdminWorkerInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;

public class AdminAddUpdateCampaign extends AppCompatActivity {

    DatePickerDialog picker;
    ImageView backarrow, info;
    String temp, temp_key;
    TextView top_text, clear_all_and_delete;

    Button campaign_btn;

    String campaign_key, campaign_id, start_date, end_date, admin_key, add_date, update_admin_key = "null", update_date = "null";
    EditText id_campaign, date_start, date_end;

    ProgressBar pr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_campaign_add_update);

        backarrow = findViewById(R.id.backsign);
        backarrow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        info = findViewById(R.id.campaign_info);


        pr = findViewById(R.id.progressBar);
        pr.setVisibility(View.INVISIBLE);

        top_text = findViewById(R.id.top_text);
        id_campaign = findViewById(R.id.campaign_id);
        date_start = findViewById(R.id.start_date);
        date_end = findViewById(R.id.end_date);
        clear_all_and_delete = findViewById(R.id.clear_all_delete);
        campaign_btn = findViewById(R.id.add_update_campaign_btn);


        clear_all_and_delete.setPaintFlags(clear_all_and_delete.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        date_start.setInputType(InputType.TYPE_NULL);
        date_end.setInputType(InputType.TYPE_NULL);

        date_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(AdminAddUpdateCampaign.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                date_start.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        date_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(AdminAddUpdateCampaign.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                date_end.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        Intent intent = getIntent();
        temp = intent.getStringExtra("Action");
        temp_key = intent.getStringExtra("campaign_key");

        if (temp.equals("Add_Campaign")) {
            top_text.setText("Add New Campaign");
            clear_all_and_delete.setText("clear all");
            campaign_btn.setText("Add");

            clear_all_and_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pr.setVisibility(View.VISIBLE);
                    clear_all();
                }
            });

            campaign_btn.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {

                    boolean connected = false;
                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                        pr.setVisibility(View.VISIBLE);
                        add_campaign();
                    } else {
                        on_internet_dailog();
                    }

                }
            });

        } else if (temp.equals("Update_Delete_Campaign")) {
            top_text.setText("Update & Delete Campaign");
            //clear_all_and_delete.setText("delete");
            clear_all_and_delete.setVisibility(View.INVISIBLE);
            campaign_btn.setText("Update");

            pr.setVisibility(View.VISIBLE);

            info.setVisibility(View.VISIBLE);
            info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AdminAddUpdateCampaign.this, AdminCampaignInfo.class);
                    intent.putExtra("campaign_key", temp_key);
                    startActivity(intent);
                }
            });

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Campaign").child(temp_key);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getValue() != null) {

                        String campaign_id = dataSnapshot.child("campaign_id").getValue().toString();
                        String campaign_start_date = dataSnapshot.child("start_date").getValue().toString();
                        String campaign_end_date = dataSnapshot.child("end_date").getValue().toString();

                        id_campaign.setText(campaign_id);
                        id_campaign.setEnabled(false);
                        date_start.setText(campaign_start_date);
                        date_end.setText(campaign_end_date);
                        pr.setVisibility(View.INVISIBLE);
                    } else {
                        pr.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "Campaign Data Not Found !!!", Toast.LENGTH_SHORT).show();
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
//                        delete_campaign(temp_key);
//                    } else {
//                        on_internet_dailog();
//                    }
//                }
//            });

            campaign_btn.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {
                    boolean connected = false;
                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                        pr.setVisibility(View.VISIBLE);
                        update_campaign(temp_key);
                    } else {
                        on_internet_dailog();
                    }
                }
            });
        }
    }

    private void clear_all() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        id_campaign.getText().clear();
        date_start.getText().clear();
        date_end.getText().clear();
        pr.setVisibility(View.INVISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void add_campaign() {

        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        campaign_id = id_campaign.getText().toString().trim();
        start_date = date_start.getText().toString().trim();
        end_date = date_end.getText().toString().trim();

        if (campaign_id.isEmpty() || campaign_id.length() < 7) {
            pr.setVisibility(View.INVISIBLE);
            id_campaign.setError("Campaign id cannot be less than 7 characters!");
            id_campaign.requestFocus();
            return;
        }
        if (start_date.isEmpty()) {
            pr.setVisibility(View.INVISIBLE);
            date_start.setError("Please enter start date");
            date_start.requestFocus();
            return;
        }
        if (end_date.isEmpty()) {
            pr.setVisibility(View.INVISIBLE);
            date_end.setError("Please enter end date");
            date_end.requestFocus();
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        add_date = dateFormat.format(new Date());

        final DatabaseReference cDatabaseRef = FirebaseDatabase.getInstance().getReference("Campaign");
        DatabaseReference newChildRef = cDatabaseRef.push();
        campaign_key = newChildRef.getKey();
        admin_key = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final AdminCampaignModal adminCampaignModal = new AdminCampaignModal(

                campaign_id, start_date, end_date, admin_key, add_date, update_admin_key, update_date, campaign_key
        );

        Query query = FirebaseDatabase.getInstance().getReference("Campaign");
        query.orderByChild("campaign_id").equalTo(campaign_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot.getValue() == null) {

                    new AlertDialog.Builder(AdminAddUpdateCampaign.this)
                            .setTitle("Add New Worker Data")
                            .setMessage("Are you want to add data. Press yes")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    cDatabaseRef.child(campaign_key).setValue(adminCampaignModal).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()) {
                                                clear_all();
                                                pr.setVisibility(View.INVISIBLE);
                                                Toast.makeText(getApplicationContext(), "Campaign Add Successfully", Toast.LENGTH_LONG).show();
                                                finish();
                                            } else {
                                                pr.setVisibility(View.INVISIBLE);
                                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                            }
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
                    Toast.makeText(getApplicationContext(), "Campaign Already Exist With the id : " + campaign_id, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

//    private void delete_campaign(final String temp_key) {
//
//        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//
//        new AlertDialog.Builder(AdminAddUpdateCampaign.this)
//                .setTitle("Dalete Data")
//                .setMessage("Are you sure want to delete campaign data. Press Yes")
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        pr.setVisibility(View.VISIBLE);
//                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
//                        databaseReference.child("Campaign").child(temp_key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if (task.isSuccessful()) {
//                                    clear_all();
//                                    pr.setVisibility(View.INVISIBLE);
//                                    Toast.makeText(getApplicationContext(), "Campaign Data Delete Successfull", Toast.LENGTH_LONG).show();
//                                    finish();
//                                } else {
//                                    pr.setVisibility(View.INVISIBLE);
//                                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
//                                }
//                            }
//                        });
//                    }
//                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        }).show();
//    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void update_campaign(final String temp_key) {

        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        update_date = dateFormat.format(new Date());

        start_date = date_start.getText().toString().trim();
        end_date = date_end.getText().toString().trim();

        if (start_date.isEmpty()) {
            pr.setVisibility(View.INVISIBLE);
            date_start.setError("Please enter start date");
            date_start.requestFocus();
            return;
        }
        if (end_date.isEmpty()) {
            pr.setVisibility(View.INVISIBLE);
            date_end.setError("Please enter end date");
            date_end.requestFocus();
            return;
        }

        update_admin_key = FirebaseAuth.getInstance().getCurrentUser().getUid();

        new AlertDialog.Builder(AdminAddUpdateCampaign.this)
                .setTitle("Update Campaign Data")
                .setMessage("Are you want to Update Campaign data. Press yes")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                        databaseReference.child("Campaign").child(temp_key).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                dataSnapshot.getRef().child("start_date").setValue(start_date);
                                dataSnapshot.getRef().child("end_date").setValue(end_date);
                                dataSnapshot.getRef().child("update_admin_key").setValue(update_admin_key);
                                dataSnapshot.getRef().child("update_date").setValue(update_date);
                                pr.setVisibility(View.INVISIBLE);
                                Toast.makeText(getApplicationContext(), "Campaign Data Update Successfully", Toast.LENGTH_LONG).show();
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

    }

    private void on_internet_dailog() {

        new AlertDialog.Builder(AdminAddUpdateCampaign.this)
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
