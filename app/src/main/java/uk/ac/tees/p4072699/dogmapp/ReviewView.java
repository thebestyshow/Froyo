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
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
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

public class ReviewView extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private GoogleMap map;
    private GoogleApiClient googleAPI;
    private LocationRequest locRequest;
    private Location location;
    private LocationManager lm;
    private Owner owner;
    private Walk w;
    private DatabaseHandler dh = new DatabaseHandler(this);
    private ArrayList<LatLng> points;
    private Polyline line;
    private ImageView p1, p2, p3, p4, p5;
    private String prevAct;
    private TextView name, comm, dis, time;
    private Button edit, remove;
    private String h, m, s;
    private int Hours, Minutes, Seconds;

    /*Intialises all TextViews,Buttons and Variables. Sets ImageViews to the corrext image's depending on the rating of the walk passed to this activity.
    * Displays the length and time taken on this walk as well as the route taken. The route is displayed on google maps within a mapfragment
    * If the Edit walk button is pressed, the user is taken to the Edit review activity
    * IF the Remove button is pressed, the walk being displayed will be deleted from the database*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_view);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapo);
        mapFragment.getMapAsync(this);
        owner = (Owner) getIntent().getSerializableExtra("owner");
        w = (Walk) getIntent().getSerializableExtra("walk");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().hasExtra("home")){
            prevAct = getIntent().getStringExtra("home");
        }else if(getIntent().hasExtra("revlist")){
            prevAct = getIntent().getStringExtra("revlist");
        }

        name = (TextView) findViewById(R.id.textView_Revname);
        comm = (TextView) findViewById(R.id.textView_Review);
        dis = (TextView) findViewById(R.id.textView_dis);
        time = (TextView) findViewById(R.id.textView_time);
        edit = (Button) findViewById(R.id.button_save2);
        remove = (Button) findViewById(R.id.button_remove);
        DecimalFormat df = new DecimalFormat("00.00");
        points = getIntent().getParcelableArrayListExtra("pointsarray");
        w.setPoints(points);

        locRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10);
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        name.setText(w.getName());
        comm.setText(w.getComment());
        dis.setText(df.format(w.getLength()) + "km");

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

        if (w.getRating() == 1) {
            p1.setImageResource(R.drawable.selected);
            p2.setImageResource(R.drawable.paw);
            p3.setImageResource(R.drawable.paw);
            p4.setImageResource(R.drawable.paw);
            p5.setImageResource(R.drawable.paw);
        } else if (w.getRating() == 2) {
            p2.setImageResource(R.drawable.selected);
            p1.setImageResource(R.drawable.selected);
            p3.setImageResource(R.drawable.paw);
            p4.setImageResource(R.drawable.paw);
            p5.setImageResource(R.drawable.paw);
        } else if (w.getRating() == 3) {
            p3.setImageResource(R.drawable.selected);
            p2.setImageResource(R.drawable.selected);
            p1.setImageResource(R.drawable.selected);
            p4.setImageResource(R.drawable.paw);
            p5.setImageResource(R.drawable.paw);
        } else if (w.getRating() == 4) {
            p4.setImageResource(R.drawable.selected);
            p2.setImageResource(R.drawable.selected);
            p3.setImageResource(R.drawable.selected);
            p1.setImageResource(R.drawable.selected);
            p5.setImageResource(R.drawable.paw);
        } else if (w.getRating() == 5) {
            p5.setImageResource(R.drawable.selected);
            p2.setImageResource(R.drawable.selected);
            p3.setImageResource(R.drawable.selected);
            p4.setImageResource(R.drawable.selected);
            p1.setImageResource(R.drawable.selected);
        }

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), EditWalk.class);
                i.putParcelableArrayListExtra("pointsarray", w.getPoints());
                w.setPoints(null);
                i.putExtra("walk", w);
                i.putExtra("owner", owner);
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

    /*checks if a menu item is pressed and if it is, a check is made to find which is the previous screen. Depending on the result
    * is the activity which the user is taken to*/
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (prevAct == "home"){
                Intent i = new Intent(getApplicationContext(),Home.class);
                i.putExtra("owner", owner);
                startActivity(i);
                return true;
            }else if (prevAct == "revList"){
                Intent i = new Intent(getApplicationContext(),ReviewList.class);
                i.putExtra("owner", owner);
                startActivity(i);
                return true;
            }else{
                onBackPressed();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    /*Redraws the polyline of the route on the google map*/
    public void redrawLine() {
        Log.d("Redraw", "Redraw line start");
        map.clear();
        PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
        int f = points.size();

        for (int i = 0; i < f; i++) {
            LatLng point = points.get(i);
            options.add(point);
        }
        line = map.addPolyline(options);
        zoomRoute(map, points);
        points.clear();
        Log.d("DEBUG", "Array after draw: " + points.toString());
        Log.d("Redraw", "Redraw line finish");
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

    /*Zooms in on the polyline of the route that was taken when this walk was saved. */
    public void zoomRoute(GoogleMap googleMap, ArrayList<LatLng> lstLatLngRoute) {

        Log.d("Zoom", "Zoom start");
        if (googleMap == null || lstLatLngRoute == null || lstLatLngRoute.isEmpty()) {
            return;
        }

        LatLngBounds currentLatLongBounds = googleMap.getProjection().getVisibleRegion().latLngBounds;
        boolean updateBounds = false;
        for (LatLng latlng : lstLatLngRoute) {
            if (!currentLatLongBounds.contains(latlng)) {
                updateBounds = true;
            }
        }

        if (updateBounds) {
            CameraUpdate cameraUpdate;

            if (lstLatLngRoute.size() == 1) {
                LatLng latlng = lstLatLngRoute.iterator().next();

                cameraUpdate = CameraUpdateFactory.newLatLng(latlng);
            } else {
                LatLngBounds.Builder builder = LatLngBounds.builder();
                for (LatLng latlng : lstLatLngRoute) {
                    builder.include(latlng);
                }

                LatLngBounds latLongBounds = builder.build();

                cameraUpdate = CameraUpdateFactory.newLatLngBounds(latLongBounds, 700, 600, 0);

                try {
                    googleMap.animateCamera(cameraUpdate, 500,
                            new GoogleMap.CancelableCallback() {
                                @Override
                                public void onFinish() {

                                }

                                @Override
                                public void onCancel() {

                                }
                            });
                } catch (IllegalStateException ex) {
                    ex.printStackTrace();
                }
            }
        }
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        Log.d("Zoom", "Zoom Finish");
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
