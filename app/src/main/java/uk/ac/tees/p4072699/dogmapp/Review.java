package uk.ac.tees.p4072699.dogmapp;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;

import java.text.DecimalFormat;
import java.text.ParseException;
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

    String e;
    String s;
    int hours;
    int min;
    double d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Review Walk");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        lisbun  = getIntent().getExtras().getBundle("bundle");
        owner = (Owner) getIntent().getSerializableExtra("owner");
        doglist = (ArrayList<Dog>) lisbun.getSerializable("ARRAYLIST");
        loc = getIntent().getParcelableArrayListExtra("locs");
        LatLng ltlg;

        for (Location l : loc){
            ltlg = new LatLng(l.getLatitude(),l.getLongitude());
            points.add(ltlg);
        }

        e = getIntent().getStringExtra("end");
        s = getIntent().getStringExtra("start");
        d = getIntent().getExtras().getDouble("dis");
        DecimalFormat df = new DecimalFormat("#.00");


        dh.addOwnerWalk(owner,Double.parseDouble(df.format(d)));
        dh.addDogWalk(doglist,Double.parseDouble(df.format(d)));

        String start = s.substring(11, 18);
        String end = e.substring(11, 18);

        SimpleDateFormat smpl = new SimpleDateFormat("HH:mm");
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = smpl.parse(start);
            endDate = smpl.parse(end);
        } catch (ParseException e1) {
        }
        long difference = endDate.getTime() - startDate.getTime();

        hours = (int) (difference / (1000 * 60 * 60));
        min = (int) (difference - (1000 * 60 * 60 * hours)) / (1000 * 60);

        Log.d("Time difference", Integer.toString(hours) + Integer.toString(min));

        final Context con = this;
        final Button save = (Button) findViewById(R.id.button_savez);
        final EditText com = (EditText) findViewById(R.id.et_comm);
        final EditText name = (EditText) findViewById(R.id.etname);
        final Button cancel = (Button) findViewById(R.id.button_cancel);
        p1 = (ImageButton) findViewById(R.id.paw_1);
        p2 = (ImageButton) findViewById(R.id.paw_2);
        p3 = (ImageButton) findViewById(R.id.paw_3);
        p4 = (ImageButton) findViewById(R.id.paw_4);
        p5 = (ImageButton) findViewById(R.id.paw_5);
        final ImageButton set = (ImageButton) findViewById(R.id.imageButton_settings);
        final TextView tv = (TextView) findViewById(R.id.textView_time);
        final TextView tvd = (TextView) findViewById(R.id.textView_distance);

        tv.setText("Hours: " + hours + " Minutes: " + min);
        tvd.setText(df.format(d));

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(con, Settings.class);
                i.putExtra("owner", dh.getOwnerHelper(owner));
                startActivity(i);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast t;
                if  (name.getText().toString().equals("")){
                    t = Toast.makeText(getApplicationContext(),"Please enter a name", Toast.LENGTH_SHORT);
                    t.show();
                }else{
                    try {
                        dh.add(new Walk(name.getText().toString(),d,paws,com.getText().toString(),Integer.valueOf(String.valueOf(hours) + String.valueOf(min)),points));
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    //dh.add(new Walk(name.getText().toString(),d,paws,com.getText().toString(),Integer.valueOf(String.valueOf(hours) + String.valueOf(min))));
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
                dh.addW(new Walk(d,(Integer.valueOf(String.valueOf(hours) + String.valueOf(min))),points));
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
}
