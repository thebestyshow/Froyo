package uk.ac.tees.p4072699.dogmapp;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.model.LatLng;
import org.json.JSONException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Review extends AppCompatActivity {
    private DatabaseHandler dh = new DatabaseHandler(this);
    private int paws;
    private Owner owner;
    private Bundle lisbun;
    private ArrayList<Location> loc;
    private ArrayList<LatLng> points = new ArrayList<LatLng>();
    private ArrayList<Dog> doglist;
    private ImageButton p1, p2, p3, p4, p5;
    private int time;
    private String hours;
    private String mins;
    private String secs;
    private double d;
    private String date;
    private String shareMessage = "test";
    private int numDogs;
    private Button save, cancel, share;
    private EditText com, name;
    private TextView tv, tvd;

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
        for (Location l : loc) {
            ltlg = new LatLng(l.getLatitude(), l.getLongitude());
            points.add(ltlg);
        }

        hours = getIntent().getStringExtra("hours");
        mins = getIntent().getStringExtra("mins");
        secs = getIntent().getStringExtra("secs");
        d = getIntent().getExtras().getDouble("dis");
        DecimalFormat df = new DecimalFormat("00.00");

        numDogs = doglist.size();
        dh.addOwnerWalk(owner, Double.parseDouble(df.format(d)));
        dh.addDogWalk(doglist, Double.parseDouble(df.format(d)));

        //setup all the buttons so that they are ready to be used
        final Context con = this;
        save = (Button) findViewById(R.id.button_save2);
        com = (EditText) findViewById(R.id.et_comm);
        name = (EditText) findViewById(R.id.etname);
        cancel = (Button) findViewById(R.id.button_cancel);
        share = (Button) findViewById(R.id.button_share);
        p1 = (ImageButton) findViewById(R.id.paw_1);
        p2 = (ImageButton) findViewById(R.id.paw_2);
        p3 = (ImageButton) findViewById(R.id.paw_3);
        p4 = (ImageButton) findViewById(R.id.paw_4);
        p5 = (ImageButton) findViewById(R.id.paw_5);
        tv = (TextView) findViewById(R.id.textView_time);
        tvd = (TextView) findViewById(R.id.textView_distance);

        //format the time to display it
        int h, m, s;
        hours = String.format("%02d", Integer.valueOf(hours));
        secs = String.format("%02d", Integer.valueOf(secs));
        mins = String.format("%02d", Integer.valueOf(mins));
        time = Integer.parseInt(hours) + Integer.parseInt(mins) + Integer.parseInt(secs);
        tv.setText("" + hours + ":" + mins + ":" + secs);
        tvd.setText(df.format(d) + "km");
        numDogs = doglist.size();

        //The message that will be shared via text
        shareMessage = ("I just walked " + (df.format(d)) +
                " km in a time of " + "" + hours + ":" + mins + ":" + secs + " and recorded my route using dogMapp. " +
                "You could be recording your dog walks too by downloading dogMapp from Google Play for free");
        if (numDogs > 0)
        {
            shareMessage = ("I just walked " + numDogs + " dogs a total of " + (df.format(d)) +
                    " km in a time of " + "" + hours + ":" + mins + ":" + secs + " and recorded my route using dogMapp. " +
                    "You could be recording your dog walks too by downloading dogMapp from Google Play for free");
        }

        /*set up the share button with options to post shareMEssage from above.*/
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

        //When save is clicked the review is saved only if there is a name
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

        //If cancel is clicked the walk is saved without a rating, comment or name
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

        //For the paw ratings
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
}


