package uk.ac.tees.p4072699.dogmapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
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
        points = getIntent().getParcelableArrayListExtra("pointsarray");
        w.setPoints(points);

        Log.d("dsa","CURRENT LATLONGS : " + points.toString());
        //points.add(l1);
        //points.add(l2);

        locRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10);
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        name.setText(w.getName());
        comm.setText(w.getComment());
        dis.setText(df.format(w.getLength()) + "km");

        int Hours, Minutes, Seconds;
        String h, m, s;
        Seconds = w.getTime();
        Minutes = Seconds / 60;
        Hours = Minutes / 60;
        Seconds = Seconds % 60;

        h = String.format("%02d", Integer.valueOf(Hours));
        s = String.format("%02d", Integer.valueOf(Seconds));
        m = String.format("%02d", Integer.valueOf(Minutes));

        time.setText(h + ":" + m + ":" + s);

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
                i.putExtra("owner",owner);
                startActivity(i);
            }
        });

        edit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), EditWalk.class);
                i.putParcelableArrayListExtra("pointsarray",w.getPoints());
                w.setPoints(null);
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


    public void redrawLine(){
        Log.d("Redraw","Redraw line start");
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
        zoomRoute(map,points);
        points.clear();
        Log.d("DEBUG","Array after draw: " + points.toString());
        Log.d("Redraw","Redraw line finish");
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            location = LocationServices.FusedLocationApi.getLastLocation(googleAPI);

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

        redrawLine();

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

        //map.getUiSettings().setScrollGesturesEnabled(false);
    }

    public void zoomRoute(GoogleMap googleMap, ArrayList<LatLng> lstLatLngRoute) {

        Log.d("Zoom","Zoom start");
        if (googleMap == null || lstLatLngRoute == null || lstLatLngRoute.isEmpty()){
            return;
        }

        LatLngBounds currentLatLongBounds = googleMap.getProjection().getVisibleRegion().latLngBounds;
        boolean updateBounds = false;
        for (LatLng latlng : lstLatLngRoute){
            if (!currentLatLongBounds.contains(latlng)){
                updateBounds = true;
            }
        }

        if (updateBounds){
            CameraUpdate cameraUpdate;

            if (lstLatLngRoute.size()==1){
                LatLng latlng = lstLatLngRoute.iterator().next();

                cameraUpdate = CameraUpdateFactory.newLatLng(latlng);
            }else {
                LatLngBounds.Builder builder = LatLngBounds.builder();
                for (LatLng latlng : lstLatLngRoute){
                    builder.include(latlng);
                }

                LatLngBounds latLongBounds = builder.build();

                cameraUpdate = CameraUpdateFactory.newLatLngBounds(latLongBounds,90);

                try{
                    googleMap.animateCamera(cameraUpdate,500,
                            new GoogleMap.CancelableCallback(){
                                @Override
                                public void onFinish(){

                                }

                                @Override
                                public void onCancel(){

                                }
                            });
                } catch (IllegalStateException ex){
                }
            }
        }

        Log.d("Zoom","Zoom Finish");
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
