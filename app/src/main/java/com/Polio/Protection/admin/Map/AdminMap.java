package com.Polio.Protection.admin.Map;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import com.Polio.Protection.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AdminMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ImageView backarrow;

    Double la, lo;
    String location_latitude, location_longitude, action, campaign_key, team_key, children_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_map);

        backarrow = findViewById(R.id.backsign);
        backarrow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Intent intent = getIntent();
        action = intent.getStringExtra("Action_map");

        if (action.equals("Single_show")) {

            location_latitude = intent.getStringExtra("location_latitude");
            location_longitude = intent.getStringExtra("location_longitude");
            la = Double.parseDouble(location_latitude);
            lo = Double.parseDouble(location_longitude);

            LatLng loc = new LatLng(la, lo);
            mMap.addMarker(new MarkerOptions().position(loc).title("Mark Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 18), 1000, null);
        }
        else if(action.equals("search")){

            team_key = intent.getStringExtra("team_key");
            campaign_key = intent.getStringExtra("campaign_key");

            Query query = FirebaseDatabase.getInstance().getReference("Campaign").child(campaign_key).child("Children");
            query.orderByChild("team_key").equalTo(team_key).addListenerForSingleValueEvent(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                            children_name = dataSnapshot1.child("children_name").getValue().toString();
                            location_latitude = dataSnapshot1.child("location_latitude").getValue().toString();
                            location_longitude = dataSnapshot1.child("location_longitude").getValue().toString();
                            la = Double.parseDouble(location_latitude);
                            lo = Double.parseDouble(location_longitude);

                            LatLng loc = new LatLng(la, lo);
                            mMap.addMarker(new MarkerOptions().position(loc).title(children_name));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 18), 3000, null);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
        else {

            campaign_key = intent.getStringExtra("campaign_key");
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Campaign").child(campaign_key).child("Children");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                            children_name = dataSnapshot1.child("children_name").getValue().toString();
                            location_latitude = dataSnapshot1.child("location_latitude").getValue().toString();
                            location_longitude = dataSnapshot1.child("location_longitude").getValue().toString();
                            la = Double.parseDouble(location_latitude);
                            lo = Double.parseDouble(location_longitude);

                            LatLng loc = new LatLng(la, lo);
                            mMap.addMarker(new MarkerOptions().position(loc).title(children_name));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 18), 3000, null);
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

        }
    }
}
