package uk.ac.tees.p4072699.dogmapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        owner = (Owner) getIntent().getSerializableExtra("owner");

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
                dh.add(new Walk("TEST", "2KM", paws, com.getText().toString()));
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
