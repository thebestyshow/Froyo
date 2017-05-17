package uk.ac.tees.p4072699.dogmapp;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Review extends AppCompatActivity {
    DatabaseHandler dh = new DatabaseHandler(this);
    int paws;
    Owner owner;
    Bundle lisbun;
    ArrayList<Location> loc;
    ArrayList<LatLng> points = new ArrayList<LatLng>();
    ArrayList<Dog> doglist;
    ImageButton p1, p2, p3, p4, p5;
    int time;
    String hours;
    String mins;
    String secs;
    double d;
    String date;
    String shareMessage = "test";
    int numDogs;
    CallbackManager callbackManager;
    LoginButton fbLogin;
    boolean loggedInFB = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Review Walk");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lisbun = getIntent().getExtras().getBundle("bundle");
        owner = (Owner) getIntent().getSerializableExtra("owner");
        doglist = (ArrayList<Dog>) lisbun.getSerializable("ARRAYLIST");
        loc = getIntent().getParcelableArrayListExtra("locs");
        LatLng ltlg;
        date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());


        FacebookSdk.sdkInitialize(getApplicationContext());
        LoginManager.getInstance().logOut();
        fbLogin = (LoginButton)findViewById(R.id.facebook_login_button);
        callbackManager = CallbackManager.Factory.create();

        fbLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });


        for (Location l : loc) {
            ltlg = new LatLng(l.getLatitude(), l.getLongitude());
            points.add(ltlg);
        }

        hours = getIntent().getStringExtra("hours");
        mins = getIntent().getStringExtra("mins");
        secs = getIntent().getStringExtra("secs");
        d = getIntent().getExtras().getDouble("dis");
        DecimalFormat df = new DecimalFormat("##.00");

        numDogs = doglist.size();

        dh.addOwnerWalk(owner, Double.parseDouble(df.format(d)));
        dh.addDogWalk(doglist, Double.parseDouble(df.format(d)));
        final Context con = this;
        final Button save = (Button) findViewById(R.id.button_savez);
        final EditText com = (EditText) findViewById(R.id.et_comm);
        final EditText name = (EditText) findViewById(R.id.etname);
        final Button cancel = (Button) findViewById(R.id.button_cancel);
        final Button share = (Button) findViewById(R.id.button_share);
        p1 = (ImageButton) findViewById(R.id.paw_1);
        p2 = (ImageButton) findViewById(R.id.paw_2);
        p3 = (ImageButton) findViewById(R.id.paw_3);
        p4 = (ImageButton) findViewById(R.id.paw_4);
        p5 = (ImageButton) findViewById(R.id.paw_5);
        final TextView tv = (TextView) findViewById(R.id.textView_time);
        final TextView tvd = (TextView) findViewById(R.id.textView_distance);


        int h, m, s;
        hours = String.format("%02d", Integer.valueOf(hours));
        secs = String.format("%02d", Integer.valueOf(secs));
        mins = String.format("%02d", Integer.valueOf(mins));
        time = Integer.parseInt(hours) + Integer.parseInt(mins) + Integer.parseInt(secs);
        tv.setText("" + hours + ":" + mins + ":" + secs);
        tvd.setText(df.format(d));

        shareMessage = ("I just walked " + (df.format(d)) +
                " km in a time of " + "" + hours + ":" + mins + ":" + secs + " and recorded my route using dogMapp. " +
                "You could be recording your dog walks too by downloading dogMapp from Google Play for free");
        if (numDogs > 0)
        {
            shareMessage = ("I just walked " + numDogs + " dogs a total of " + (df.format(d)) +
                    " km in a time of " + "" + hours + ":" + mins + ":" + secs + " and recorded my route using dogMapp. " +
                    "You could be recording your dog walks too by downloading dogMapp from Google Play for free");
        }
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "DogMapp");
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "Share via"));

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast t;
                if (name.getText().toString().equals("")) {
                    t = Toast.makeText(getApplicationContext(), "Please enter a name", Toast.LENGTH_SHORT);
                    t.show();
                } else {
                    try {
                        dh.add(new Walk(name.getText().toString(), d, paws, com.getText().toString(), time, points, date));
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    Intent intent = new Intent(con, Home.class);
                    intent.putExtra("owner", dh.getOwnerHelper(owner));
                    startActivity(intent);
                    setContentView(R.layout.activity_home);
                    finish();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dh.addW(new Walk(d, time, points, date));
                Intent intent = new Intent(con, Home.class);
                intent.putExtra("owner", dh.getOwnerHelper(owner));
                startActivity(intent);
                setContentView(R.layout.activity_home);
                finish();
            }
        });

        p1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                paws = 1;
                p1.setImageResource(R.drawable.selected);
                p2.setImageResource(R.drawable.paw);
                p3.setImageResource(R.drawable.paw);
                p4.setImageResource(R.drawable.paw);
                p5.setImageResource(R.drawable.paw);
            }
        });


        p2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                paws = 2;
                p2.setImageResource(R.drawable.selected);
                p1.setImageResource(R.drawable.selected);
                p3.setImageResource(R.drawable.paw);
                p4.setImageResource(R.drawable.paw);
                p5.setImageResource(R.drawable.paw);
            }
        });

        p3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                paws = 3;
                p3.setImageResource(R.drawable.selected);
                p2.setImageResource(R.drawable.selected);
                p1.setImageResource(R.drawable.selected);
                p4.setImageResource(R.drawable.paw);
                p5.setImageResource(R.drawable.paw);
            }
        });

        p4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                paws = 4;
                p4.setImageResource(R.drawable.selected);
                p2.setImageResource(R.drawable.selected);
                p3.setImageResource(R.drawable.selected);
                p1.setImageResource(R.drawable.selected);
                p5.setImageResource(R.drawable.paw);
            }
        });

        p5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                paws = 5;
                p5.setImageResource(R.drawable.selected);
                p2.setImageResource(R.drawable.selected);
                p3.setImageResource(R.drawable.selected);
                p4.setImageResource(R.drawable.selected);
                p1.setImageResource(R.drawable.selected);
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}


