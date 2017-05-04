package uk.ac.tees.p4072699.dogmapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.DecimalFormat;

public class DogProfile extends AppCompatActivity {
    DatabaseHandler dh = new DatabaseHandler(this);
    Dog d;
    Owner owner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        owner = (Owner) getIntent().getSerializableExtra("owner");
        d = (Dog) getIntent().getSerializableExtra("dog");
        final TextView name = (TextView) findViewById(R.id.Dog_name);
        final TextView totWalks = (TextView) findViewById(R.id.Dog_totwalks);
        final TextView avgWalks = (TextView) findViewById(R.id.Dog_avgwalks);
        final Context con = getApplicationContext();
        final ImageButton set = (ImageButton) findViewById(R.id.imageButton_settings);
        final Button ret = (Button) findViewById(R.id.But_return);

        totWalks.setText(Integer.toString(d.getTotwalks()));
        name.setText(d.getName());
        DecimalFormat df = new DecimalFormat("#.00");
        String avg = df.format(d.getTotdistance()/d.getTotwalks());
        avgWalks.setText(avg + "KM");

        ret.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                onBackPressed();
            }
        });

        set.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent i = new Intent(con, Settings.class);
                i.putExtra("owner",dh.getOwnerHelper(owner));
                startActivity(i);
            }
        });
    }

}
