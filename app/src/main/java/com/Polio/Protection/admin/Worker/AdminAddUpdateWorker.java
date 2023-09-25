package com.Polio.Protection.admin.Worker;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.Polio.Protection.R;
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

public class AdminAddUpdateWorker extends AppCompatActivity {

    ImageView backarrow, worker_info;
    String temp, temp_key;
    TextView top_text, clear_all_and_delete;
    Button worker_btn;

    EditText id_worker, name_worker, cell_number_worker, date_of_birth_worker;
    private RadioGroup radioGroup;

    String worker_id, worker_key, worker_name, worker_cell_number, worker_date_of_birth, Gender,
            worker_add_admin_key, worker_add_date, worker_update_admin_key = "null", worker_update_date = "null", worker_team_id = "null";
    DatePickerDialog picker;

    ProgressBar pr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_worker_add_update);

        worker_info = findViewById(R.id.worker_info);
        backarrow = findViewById(R.id.backsign);
        backarrow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        pr = findViewById(R.id.progressBar);
        pr.setVisibility(View.INVISIBLE);

        top_text = findViewById(R.id.top_text);
        clear_all_and_delete = findViewById(R.id.clear_all_and_delete);
        worker_btn = findViewById(R.id.worker_btn);

        clear_all_and_delete.setPaintFlags(clear_all_and_delete.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        id_worker = findViewById(R.id.worker_id);
        name_worker = findViewById(R.id.worker_name);
        cell_number_worker = findViewById(R.id.worker_cell_number);
        date_of_birth_worker = findViewById(R.id.worker_date_of_birth);
        radioGroup = findViewById(R.id.worker_gender);

        date_of_birth_worker.setInputType(InputType.TYPE_NULL);
        date_of_birth_worker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(AdminAddUpdateWorker.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                date_of_birth_worker.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });


        Intent intent = getIntent();
        temp = intent.getStringExtra("Action");
        temp_key = intent.getStringExtra("worker_key");

        if (temp.equals("Add_Worker")) {
            top_text.setText("Add New Worker");
            clear_all_and_delete.setText("clear all");
            worker_btn.setText("Add");

            clear_all_and_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pr.setVisibility(View.VISIBLE);
                    clear_all();
                }
            });

            worker_btn.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {

                    boolean connected = false;
                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                        pr.setVisibility(View.VISIBLE);
                        add_worker();
                    } else {
                        on_internet_dailog();
                    }

                }
            });

        } else if (temp.equals("Update_Delete_Worker")) {
            top_text.setText("Update & Delete Worker");
            //clear_all_and_delete.setText("delete");
            clear_all_and_delete.setVisibility(View.INVISIBLE);
            worker_btn.setText("Update");

            worker_info.setVisibility(View.VISIBLE);
            worker_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AdminAddUpdateWorker.this, AdminWorkerInfo.class);
                    intent.putExtra("worker_key", temp_key);
                    startActivity(intent);
                }
            });



            pr.setVisibility(View.VISIBLE);

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Worker").child(temp_key);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getValue() != null) {

                        String worker_id = dataSnapshot.child("worker_id").getValue().toString();
                        String worker_name = dataSnapshot.child("worker_name").getValue().toString();
                        String worker_cell_number = dataSnapshot.child("worker_cell_number").getValue().toString();
                        String worker_date_of_birth = dataSnapshot.child("worker_date_of_birth").getValue().toString();
                        String worker_gender = dataSnapshot.child("worker_gender").getValue().toString();

                        id_worker.setText(worker_id);
                        id_worker.setEnabled(false);
                        name_worker.setText(worker_name);
                        cell_number_worker.setText(worker_cell_number);
                        date_of_birth_worker.setText(worker_date_of_birth);

                        if (worker_gender.equals("Male")) {
                            ((RadioButton) radioGroup.getChildAt(0)).setChecked(true);

                        } else if (worker_gender.equals("Female")) {
                            ((RadioButton) radioGroup.getChildAt(1)).setChecked(true);
                        }

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

//            clear_all_and_delete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    boolean connected = false;
//                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//                    if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
//                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
//                        delete_worker(temp_key);
//                    } else {
//                        on_internet_dailog();
//                    }
//                }
//            });

            worker_btn.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {
                    boolean connected = false;
                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                        pr.setVisibility(View.VISIBLE);
                        update_worker(temp_key);
                    } else {
                        on_internet_dailog();
                    }
                }
            });

        } else {
            Toast.makeText(getApplicationContext(), "SomeThing Wrong Try Again !!!", Toast.LENGTH_LONG).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void add_worker() {

        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        radioGroup.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        RadioButton radioButton = group.findViewById(checkedId);
                    }
                });

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        worker_add_date = dateFormat.format(new Date());

        int selectedId = radioGroup.getCheckedRadioButtonId();

        worker_id = id_worker.getText().toString().trim();
        worker_name = name_worker.getText().toString().trim();
        worker_cell_number = cell_number_worker.getText().toString().trim();
        worker_date_of_birth = date_of_birth_worker.getText().toString().trim();


        if (worker_id.isEmpty() || worker_id.length() < 8) {
            pr.setVisibility(View.INVISIBLE);
            id_worker.setError("Worker Id cannot be less than 7 characters!");
            id_worker.requestFocus();
            return;
        }
        if (worker_name.isEmpty()) {
            pr.setVisibility(View.INVISIBLE);
            name_worker.setError("Please write father name");
            name_worker.requestFocus();
            return;
        }
        if (worker_cell_number.isEmpty() || worker_cell_number.length() < 11) {
            pr.setVisibility(View.INVISIBLE);
            cell_number_worker.setError("Cell Number cannot be less than 11 characters!");
            cell_number_worker.requestFocus();
            return;
        }
        if (worker_date_of_birth.isEmpty()) {
            pr.setVisibility(View.INVISIBLE);
            date_of_birth_worker.setError("Please Enter Date of Birth");
            date_of_birth_worker.requestFocus();
            return;
        }

        if (selectedId == -1) {
            pr.setVisibility(View.INVISIBLE);
            Toast.makeText(AdminAddUpdateWorker.this, "Please selete gender", Toast.LENGTH_SHORT).show();
        } else {
            RadioButton radioButton = radioGroup.findViewById(selectedId);
            Gender = radioButton.getText().toString().trim();

            Query query = FirebaseDatabase.getInstance().getReference("Worker");
            query.orderByChild("worker_id").equalTo(worker_id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getValue() == null) {

                        new AlertDialog.Builder(AdminAddUpdateWorker.this)
                                .setTitle("Add New Worker Data")
                                .setMessage("Are you want to add data. Press yes")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        DatabaseReference wDatabaseRef = FirebaseDatabase.getInstance().getReference("Worker");
                                        DatabaseReference newChildRef = wDatabaseRef.push();
                                        worker_key = newChildRef.getKey();
                                        worker_add_admin_key = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                        AdminWorkerModal adminWorkerModal = new AdminWorkerModal(

                                                worker_id, worker_key, worker_name, worker_cell_number, worker_date_of_birth, Gender, worker_add_admin_key, worker_add_date, worker_update_admin_key, worker_update_date, worker_team_id
                                        );

                                        wDatabaseRef.child(worker_key).setValue(adminWorkerModal).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    clear_all();
                                                    pr.setVisibility(View.INVISIBLE);
                                                    Toast.makeText(getApplicationContext(), "Worker Data Add Successfully", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getApplicationContext(), "Worker Data Already Exist With the ID !!! " + worker_id, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }

    private void clear_all() {

        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        id_worker.getText().clear();
        name_worker.getText().clear();
        cell_number_worker.getText().clear();
        date_of_birth_worker.getText().clear();
        radioGroup.clearCheck();

        pr.setVisibility(View.INVISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void update_worker(final String temp_key) {

        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        radioGroup.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        RadioButton radioButton = group.findViewById(checkedId);
                    }
                });

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        worker_update_date = dateFormat.format(new Date());

        int selectedId = radioGroup.getCheckedRadioButtonId();

        worker_name = name_worker.getText().toString().trim();
        worker_cell_number = cell_number_worker.getText().toString().trim();
        worker_date_of_birth = date_of_birth_worker.getText().toString().trim();


        if (worker_name.isEmpty()) {
            pr.setVisibility(View.INVISIBLE);
            name_worker.setError("Please write father name");
            name_worker.requestFocus();
            return;
        }
        if (worker_cell_number.isEmpty() || worker_cell_number.length() < 11) {
            pr.setVisibility(View.INVISIBLE);
            cell_number_worker.setError("Cell Number cannot be less than 11 characters!");
            cell_number_worker.requestFocus();
            return;
        }
        if (worker_date_of_birth.isEmpty()) {
            pr.setVisibility(View.INVISIBLE);
            date_of_birth_worker.setError("Please Enter Date of Birth");
            date_of_birth_worker.requestFocus();
            return;
        }

        if (selectedId == -1) {
            pr.setVisibility(View.INVISIBLE);
            Toast.makeText(AdminAddUpdateWorker.this, "Please selete gender", Toast.LENGTH_SHORT).show();
        } else {
            RadioButton radioButton = radioGroup.findViewById(selectedId);
            Gender = radioButton.getText().toString().trim();
            worker_update_admin_key = FirebaseAuth.getInstance().getCurrentUser().getUid();

            new AlertDialog.Builder(AdminAddUpdateWorker.this)
                    .setTitle("Update Worker Data")
                    .setMessage("Are you want to Update Worker data. Press yes")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                            databaseReference.child("Worker").child(temp_key).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    dataSnapshot.getRef().child("worker_name").setValue(worker_name);
                                    dataSnapshot.getRef().child("worker_cell_number").setValue(worker_cell_number);
                                    dataSnapshot.getRef().child("worker_date_of_birth").setValue(worker_date_of_birth);
                                    dataSnapshot.getRef().child("worker_gender").setValue(Gender);
                                    dataSnapshot.getRef().child("worker_update_admin_key").setValue(worker_update_admin_key);
                                    dataSnapshot.getRef().child("worker_update_date").setValue(worker_update_date);
                                    pr.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getApplicationContext(), "Worker Data Update Successfully", Toast.LENGTH_LONG).show();

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
    }

//    private void delete_worker(final String temp_key) {
//
//        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//
//        new AlertDialog.Builder(AdminAddUpdateWorker.this)
//                .setTitle("Dalete Data")
//                .setMessage("Are you sure want to delete worker data. Press Yes")
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        pr.setVisibility(View.VISIBLE);
//                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
//                        databaseReference.child("Worker").child(temp_key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if (task.isSuccessful()) {
//                                    clear_all();
//                                    pr.setVisibility(View.INVISIBLE);
//                                    Toast.makeText(getApplicationContext(), "Worker Data Delete Successfull", Toast.LENGTH_LONG).show();
//
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
//
//    }

    private void on_internet_dailog() {

        new AlertDialog.Builder(AdminAddUpdateWorker.this)
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
