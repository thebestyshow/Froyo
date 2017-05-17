package uk.ac.tees.p4072699.dogmapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.location.LocationListener;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private GoogleMap map;
    private GoogleApiClient googleAPI;
    private LocationRequest locRequest;
    private Location prevLocation;
    private Location location;
    private LocationManager lm;
    private Owner owner;
    private Bundle lisbun;
    private DatabaseHandler dh = new DatabaseHandler(this);
    private double totaldis;
    private ArrayList<LatLng> points;
    private Polyline line;
    private LatLng oldlatlng;
    private int maptype;
    private TextView tv, dist;
    private long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L;
    private int Seconds, Minutes, Hours, MilliSeconds;
    private Button rev;
    private ImageButton set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Map");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        points = new ArrayList<LatLng>();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //setup the time and distance text views
        tv = (TextView) findViewById(R.id.timer);
        dist = (TextView)findViewById(R.id.tv_distance);
        StartTime = SystemClock.uptimeMillis();
        tv.postDelayed(runnable, 0);
        dist.postDelayed(runnable, 0);

        lisbun = getIntent().getExtras().getBundle("bundle");
        ArrayList<Location> loc = getIntent().getParcelableArrayListExtra("locs");
        if (loc == null) {
        } else if (!loc.isEmpty()) {
            for (Location l : loc) {
                points.add(new LatLng(l.getLatitude(), l.getLongitude()));
            }
            redrawLine();
        } else {
            Log.d("ERROR", "Empty location array");
        }

        //check that the permissions have being allowed
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkLocationPermission()) {
                locRequest = LocationRequest.create()
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                        .setInterval(10);
                lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                lm.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 400, 1, this);
            }
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final Context con = this;
        rev = (Button) findViewById(R.id.button_revScr);
        set = (ImageButton) findViewById(R.id.imageButton_settings);
        owner = (Owner) getIntent().getSerializableExtra("owner");
        maptype = getIntent().getIntExtra("map", 0);
        totaldis = getIntent().getDoubleExtra("dis", 0);
        final String s = getIntent().getStringExtra("start");

        //when the walk has finished pass all the details to the review screen to be processed
        rev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Location> locarr = new ArrayList<Location>();
                Location loc;

                TimeBuff += MillisecondTime;
                tv.removeCallbacks(runnable);
                dist.removeCallbacks(runnable);

                for (LatLng ll : points) {
                    loc = new Location("");
                    loc.setLatitude(ll.latitude);
                    loc.setLongitude(ll.longitude);
                    locarr.add(loc);
                }
                map.moveCamera(CameraUpdateFactory.zoomOut());
                Intent i = new Intent(con, Review.class);
                i.putExtra("owner", dh.getOwnerHelper(owner));
                i.putExtra("hours", Integer.toString(Hours));
                i.putExtra("mins", Integer.toString(Minutes));
                i.putExtra("secs", Integer.toString(Seconds));
                i.putParcelableArrayListExtra("locs", locarr);
                i.putExtra("dis", totaldis);
                i.putExtra("bundle", lisbun);
                MillisecondTime = 0L;
                StartTime = 0L;
                TimeBuff = 0L;
                UpdateTime = 0L;
                Seconds = 0;
                Minutes = 0;
                MilliSeconds = 0;
                startActivity(i);
            }
        });

        //to access the settings to change the map type
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Location> locarr = new ArrayList<Location>();
                Location loc;

                for (LatLng ll : points) {
                    loc = new Location("");
                    loc.setLatitude(ll.latitude);
                    loc.setLongitude(ll.longitude);
                    locarr.add(loc);
                }
                Intent i = new Intent(con, MapSettings.class);
                i.putExtra("mt", maptype);
                i.putExtra("dis", totaldis);
                i.putParcelableArrayListExtra("locs", locarr);
                i.putExtra("start", s);
                i.putExtra("owner", dh.getOwnerHelper(owner));
                i.putExtra("bundle", lisbun);
                startActivity(i);
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //setup the map
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

    //change the map type through settings
    public void setMapType() {
        int i = maptype;
        Log.d("maptype", Integer.toString(i));
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

    //when the map is connected
    @Override
    public void onConnected(Bundle bundle) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            location = LocationServices.FusedLocationApi.getLastLocation(googleAPI);
            if (location == null) {
            } else {
                handleNewLocation(location);
            }
        }
    }

    //if we wanted to pause the map connection for some reason
    @Override
    public void onConnectionSuspended(int i) {

    }

    //when the location has changed this puts the points down
    private void handleNewLocation(Location location) {
        Log.d("loc", location.toString());
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();

        LatLng newlatLng = new LatLng(currentLatitude, currentLongitude);
        if (prevLocation != null) {
            oldlatlng = new LatLng(prevLocation.getLatitude(), prevLocation.getLongitude());
            totaldis += CalculationByDistance(oldlatlng, newlatLng);
        }

        MarkerOptions options = new MarkerOptions()
                .position(newlatLng);
        map.addMarker(options);
        float f = 16;
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(newlatLng, f));
        points.add(newlatLng);
        redrawLine();
        prevLocation = location;
    }

    //used to draw the polyline
    public void redrawLine() {
        try {
            map.clear();
            PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
            int f = points.size();

            for (int i = 0; i < f; i++) {
                LatLng point = points.get(i);
                options.add(point);
            }
            line = map.addPolyline(options);
        } catch (RuntimeException e) {
            Log.d("ERROR", "Redraw line error");
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("loc", "changed");
        handleNewLocation(location);
    }

    //calculate the distance between the points
    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Miles   " + meterInDec);
        return Radius * c;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    //used to check the permissions
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (googleAPI == null) {
                            buildGoogleApiClient();
                        }
                        map.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this, "Permission Deined", Toast.LENGTH_LONG).show();
                }
                finish();
                startActivity(getIntent());
                return;
            }
        }
    }

    //this sets the timer and displays the time and distance
    //Time from http://www.android-examples.com/android-create-stopwatch-example-tutorial-in-android-studio/
    public Runnable runnable = new Runnable() {
        public void run() {
            MillisecondTime = SystemClock.uptimeMillis() - StartTime;
            UpdateTime = TimeBuff + MillisecondTime;
            Seconds = (int) (UpdateTime / 1000);
            Minutes = Seconds / 60;
            Hours = Minutes / 60;
            Seconds = Seconds % 60;
            MilliSeconds = (int) (UpdateTime % 1000);
            tv.setText("" + Hours + ":"
                    + String.format("%02d", Minutes) + ":"
                    + String.format("%02d", Seconds));
            tv.postDelayed(this, 0);
            String n = String.format("%.2f", totaldis);
            dist.setText(n);
        }
    };
}
