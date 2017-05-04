package uk.ac.tees.p4072699.dogmapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Review extends AppCompatActivity {
    DatabaseHandler dh = new DatabaseHandler(this);
    int paws;
    String comments;
    Owner owner;
    ImageButton p1;
    ImageButton p2;
    ImageButton p3;
    ImageButton p4;
    ImageButton p5;
    String e;
    String s;
    int hours;
    int min;
    String d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        owner = (Owner) getIntent().getSerializableExtra("owner");
        e = getIntent().getStringExtra("end");
        s = getIntent().getStringExtra("start");
        d = getIntent().getStringExtra("dis");

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
        final Button save = (Button) findViewById(R.id.button_save);
        final EditText com = (EditText) findViewById(R.id.editText);
        final Button cancel = (Button) findViewById(R.id.button_cancel);
        p1 = (ImageButton) findViewById(R.id.paw_1);
        p2 = (ImageButton) findViewById(R.id.paw_2);
        p3 = (ImageButton) findViewById(R.id.paw_3);
        p4 = (ImageButton) findViewById(R.id.paw_4);
        p5 = (ImageButton) findViewById(R.id.paw_5);
        final ImageButton set = (ImageButton) findViewById(R.id.imageButton_settings);
        final TextView tv = (TextView) findViewById(R.id.textView_time);

        tv.setText("Hours: " + hours + " Minutes: " + min);

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
                comments = com.getText().toString();
                dh.add(new Walk("TEST", Double.parseDouble(d), paws, com.getText().toString(), (Integer.valueOf(String.valueOf(hours) + String.valueOf(min)))));
                Intent intent = new Intent(con, Home.class);
                intent.putExtra("owner", dh.getOwnerHelper(owner));
                startActivity(intent);
                setContentView(R.layout.activity_home);
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dh.addW(new Walk(Double.parseDouble(d), (Integer.valueOf(String.valueOf(hours) + String.valueOf(min)))));
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
