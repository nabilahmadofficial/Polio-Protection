package com.Polio.Protection.team.MarkChildren;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.Polio.Protection.R;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Date;

public class TeamMarkChildrenMark extends AppCompatActivity {

    ProgressBar pr;

    String name_child, name_child_father, cnic_child_father, gender_child_, date_of_birth_child, address_child;
    TextView child_name, child_father_name, child_father_cnic, child_gender, child_date_of_birth, child_address;
    Button choice_image, mark_ok;
    ImageView backarrow, imageView;

    private static final int GALLERY_REQUEST = 1889;

    String campaign_key, campaign_id, father_cnic, children_key, children_name, date_time, team_key, Location_latitude, Location_longitude, picture;

    private Uri filePath;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_mark_children_mark);

        backarrow = findViewById(R.id.backsign);
        backarrow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        pr = findViewById(R.id.progressBar);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        child_name = findViewById(R.id.mark_children_child_name);
        child_father_name = findViewById(R.id.mark_children_father_name);
        child_father_cnic = findViewById(R.id.mark_children_father_cnic);
        child_gender = findViewById(R.id.mark_children_gender);
        child_date_of_birth = findViewById(R.id.mark_children_date_of_birth);
        child_address = findViewById(R.id.mark_children_addres);
        imageView = findViewById(R.id.mark_children_pitures);
        choice_image = findViewById(R.id.mark_children_choose_image_btn);
        mark_ok = findViewById(R.id.mark_children_mark_btn);

        Intent intent = getIntent();
        campaign_key = intent.getStringExtra("campaign_key");
        campaign_id = intent.getStringExtra("campaign_id");
        father_cnic = intent.getStringExtra("father_cnic");
        children_key = intent.getStringExtra("children_key");

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            display_children_data(father_cnic, children_key);
        } else {
            on_internet_dailog();
        }

        choice_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_select();
            }
        });

        ActivityCompat.requestPermissions(this, new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        mark_ok.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
//                boolean connected = false;
//                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
//                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
//                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//                        OnGPS();
//                    } else {
//                        pr.setVisibility(View.VISIBLE);
//                       // mark_children(campaign_key, father_cnic, children_key);
//                        uploadimage();
//                    }
//                } else {
//                    on_internet_dailog();
//                }

                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(TeamMarkChildrenMark.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
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
                            mark_check(campaign_key, father_cnic, children_key);
                        }
                    } else {
                        on_internet_dailog();
                    }
                }
            }
        });
    }

    public void display_children_data(final String cnic_father, final String key_children) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Parent").child(cnic_father);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {

                    name_child_father = dataSnapshot.child("fatherName").getValue().toString();
                    cnic_child_father = dataSnapshot.child("fatherCnic").getValue().toString();
                    address_child = dataSnapshot.child("address").getValue().toString();

                    Query query = FirebaseDatabase.getInstance().getReference("Children");
                    query.orderByChild("child_key").equalTo(key_children).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot1) {

                            if (dataSnapshot1.getValue() != null) {

                                name_child = dataSnapshot1.child(children_key).child("child_Name").getValue().toString();
                                date_of_birth_child = dataSnapshot1.child(children_key).child("dateofBirth").getValue().toString();
                                gender_child_ = dataSnapshot1.child(children_key).child("gender").getValue().toString();

                                child_name.setText(name_child);
                                child_father_name.setText(name_child_father);
                                child_father_cnic.setText(cnic_child_father);
                                child_date_of_birth.setText(date_of_birth_child);
                                child_gender.setText(gender_child_);
                                child_address.setText(address_child);
                                pr.setVisibility(View.INVISIBLE);
                            } else {
                                pr.setVisibility(View.INVISIBLE);
                                Toast.makeText(getApplicationContext(), "Some Thing was Wrong!!!", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                } else {
                    pr.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Some Thing was Wrong!!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void mark_check(final String campaign_key, final String father_cnic, final String children_key) {


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Campaign").child(campaign_key).child("Children").child(children_key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() == null) {
                    getCurrentLocation(campaign_key, father_cnic, children_key, name_child);
                } else {
                    pr.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Children Already Mark !!!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                pr.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getCurrentLocation(final String campaign_key, final String father_cnic, final String children_key, final String children_name) {

        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(TeamMarkChildrenMark.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {

                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(TeamMarkChildrenMark.this)
                                .removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int latestLocationIndex = locationResult.getLocations().size() - 1;

                            double latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                            double longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                            Location_latitude = String.valueOf(latitude);
                            Location_longitude = String.valueOf(longitude);
                            Log.i("Location_latitude", Location_latitude);
                            Log.i("Location_longitude", Location_longitude);
                            uploadimage(campaign_key, father_cnic, children_key, children_name, Location_latitude, Location_longitude);
                        }
                    }
                }, Looper.getMainLooper());

    }

    private void uploadimage(final String campaign_key, final String father_cnic, final String children_key, final String children_name, final String Location_latitude, final String Location_longitude) {

        if (filePath != null) {

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading.....");
            progressDialog.show();

            final StorageReference reference = storageReference.child("images/" + campaign_id + "/" + children_key);
            reference.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {


                            pr.setVisibility(View.VISIBLE);
                            picture = reference.getDownloadUrl().toString();
                            mark_children(campaign_key, father_cnic, children_name, children_key, uri.toString(), Location_latitude, Location_longitude);
                        }
                    });

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                    pr.setVisibility(View.INVISIBLE);
                    double progres = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded " + (int) progres + "%");
                }
            });
        } else {
            pr.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), "Please Select Image!!!", Toast.LENGTH_LONG).show();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void mark_children(final String campaign_key, final String father_cnic, final String children_name, final String children_key, final String picture, final String Location_latitude, final String Location_longitude) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        date_time = dateFormat.format(new Date());
        team_key = FirebaseAuth.getInstance().getCurrentUser().getUid();


        DatabaseReference cDatabaseRef = FirebaseDatabase.getInstance().getReference("Campaign").child(campaign_key).child("Children");
        TeamMarkChildrenMarkModal teamMarkChildrenMarkModal = new TeamMarkChildrenMarkModal(
                children_key, children_name, father_cnic, Location_latitude, Location_longitude, date_time, picture, team_key
        );

        cDatabaseRef.child(children_key).setValue(teamMarkChildrenMarkModal).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    pr.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Children Mark Successfully !!!", Toast.LENGTH_LONG).show();
                } else {
                    pr.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void image_select() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

    private void on_internet_dailog() {
        new AlertDialog.Builder(TeamMarkChildrenMark.this)
                .setTitle("No Internet Connection")
                .setMessage("You need to have Mobile Data or wifi to access this. Press Yes")
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


