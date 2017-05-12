package uk.ac.tees.p4072699.dogmapp;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ReviewView extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener  {

    private GoogleMap map;
    GoogleApiClient googleAPI;
    LocationRequest locRequest;
    Location location;
    LocationManager lm;
    Owner owner;
    Walk w;
    DatabaseHandler dh = new DatabaseHandler(this);
    ArrayList<LatLng> points;
    Polyline line;
    int maptype;
    ImageView p1, p2, p3, p4, p5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_view);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapo);
        mapFragment.getMapAsync(this);
        owner = (Owner) getIntent().getSerializableExtra("owner");
        w = (Walk) getIntent().getSerializableExtra("walk");
        final TextView name = (TextView) findViewById(R.id.textView_Revname);
        final TextView comm = (TextView) findViewById(R.id.textView_Review);
        final TextView dis = (TextView) findViewById(R.id.textView_dis);
        final TextView time = (TextView) findViewById(R.id.textView_time);
        final Button retur = (Button) findViewById(R.id.button_return);
        final Button edit = (Button) findViewById(R.id.button_savez);
        final Button remove = (Button) findViewById(R.id.button_remove);
        DecimalFormat df = new DecimalFormat("#.00");
        points = w.getPoints();

        locRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10);
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        name.setText(w.getName());
        comm.setText(w.getComment());
        dis.setText(df.format(w.getLength()));
        time.setText(String.valueOf(w.getTime()));
        p1 = (ImageView) findViewById(R.id.paw_1);
        p2 = (ImageView) findViewById(R.id.paw_2);
        p3 = (ImageView) findViewById(R.id.paw_3);
        p4 = (ImageView) findViewById(R.id.paw_4);
        p5 = (ImageView) findViewById(R.id.paw_5);

        if (w.getRating() == 1){
            p1.setImageResource(R.drawable.selected);
            p2.setImageResource(R.drawable.paw);
            p3.setImageResource(R.drawable.paw);
            p4.setImageResource(R.drawable.paw);
            p5.setImageResource(R.drawable.paw);
        }else if(w.getRating() ==2){
            p2.setImageResource(R.drawable.selected);
            p1.setImageResource(R.drawable.selected);
            p3.setImageResource(R.drawable.paw);
            p4.setImageResource(R.drawable.paw);
            p5.setImageResource(R.drawable.paw);
        }else if(w.getRating() ==3){
            p3.setImageResource(R.drawable.selected);
            p2.setImageResource(R.drawable.selected);
            p1.setImageResource(R.drawable.selected);
            p4.setImageResource(R.drawable.paw);
            p5.setImageResource(R.drawable.paw);
        }else if(w.getRating() ==4){
            p4.setImageResource(R.drawable.selected);
            p2.setImageResource(R.drawable.selected);
            p3.setImageResource(R.drawable.selected);
            p1.setImageResource(R.drawable.selected);
            p5.setImageResource(R.drawable.paw);
        }else if(w.getRating() ==5){
            p5.setImageResource(R.drawable.selected);
            p2.setImageResource(R.drawable.selected);
            p3.setImageResource(R.drawable.selected);
            p4.setImageResource(R.drawable.selected);
            p1.setImageResource(R.drawable.selected);
        }

        retur.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent i = new Intent(getApplicationContext(),ReviewList.class);
                i.putExtra("walk",w);
                i.putExtra("owner",owner);
                startActivity(i);
            }
        });

        edit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), EditWalk.class);
                i.putExtra("walk",w);
                i.putExtra("owner",owner);
                startActivity(i);
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dh.removeWalk(w.getId());
                Intent intent = new Intent(getApplicationContext(), ReviewList.class);
                intent.putExtra("owner", dh.getOwnerHelper(owner));
                startActivity(intent);
            }
        });
    }

    public static Bitmap getImage(byte[] image) {
        if (image == null){
            return null;
        }else{
            return BitmapFactory.decodeByteArray(image, 0, image.length);
        }
    }

    public void redrawLine(){
        map.clear();
        PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
        int f = points.size();

        for (int i = 0; i < f; i++) {
            LatLng point = points.get(i);
            options.add(point);
        }
        //Something goes here. Something to do with markers
        //http://stackoverflow.com/questions/30249920/how-to-draw-path-as-i-move-starting-from-my-current-location-using-google-maps
        line = map.addPolyline(options);
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            location = LocationServices.FusedLocationApi.getLastLocation(googleAPI);
            if (location == null) {
            } else {
                redrawLine();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        setMapType();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                map.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            map.setMyLocationEnabled(true);
        }
    }

    public void setMapType() {
        int i = maptype;
        if (i == 0) {
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        } else if (i == 1) {
            map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        } else if (i == 2) {
            map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        } else {
            map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        googleAPI = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleAPI.connect();
    }
}
