package com.rbhagat.admincg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class dustbinMap extends AppCompatActivity implements OnMapReadyCallback {

    ImageView back_arrow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        back_arrow=findViewById(R.id.backArrowm);

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(dustbinMap.this,dustbinActivity.class);
                startActivity(intent);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        double latitude,longitude;
        latitude=getIntent().getDoubleExtra("lat",0.0);
        longitude=getIntent().getDoubleExtra("lon",0.0);

        LatLng coordinates = new LatLng(latitude, longitude); // Replace with your coordinates
        googleMap.addMarker(new MarkerOptions().position(coordinates).title("Waste Location"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 15)); // Zoom level 15 (adjust as needed)
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }
}
