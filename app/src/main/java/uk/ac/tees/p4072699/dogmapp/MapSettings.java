package uk.ac.tees.p4072699.dogmapp;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import java.util.ArrayList;

public class MapSettings extends AppCompatActivity {
    Owner owner;
    int maptype;
    Bundle lisbun;
    DatabaseHandler dh = new DatabaseHandler(this);
    Double totaldis;
    String s;
    ArrayList<Location> locarr = new ArrayList<Location>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Map Settings");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final Context con = this;
        final Button retur = (Button) findViewById(R.id.button_return);
        final Button save = (Button) findViewById(R.id.button_savez);
        owner = (Owner) getIntent().getSerializableExtra("owner");
        lisbun = getIntent().getExtras().getBundle("bundle");

        totaldis = getIntent().getDoubleExtra("dis", 0);
        s = getIntent().getStringExtra("start");
        if (!getIntent().getParcelableArrayListExtra("locs").isEmpty()) {
            locarr = getIntent().getParcelableArrayListExtra("locs");
        }
        maptype = getIntent().getIntExtra("mt", 0);

        RadioButton n = (RadioButton) findViewById(R.id.radioButton_normal);
        RadioButton t = (RadioButton) findViewById(R.id.radioButton_terrain);
        RadioButton sa = (RadioButton) findViewById(R.id.radioButton_satellite);
        RadioButton h = (RadioButton) findViewById(R.id.radioButton_hybrid);

        if(maptype == 0) {
            n.setChecked(true);
        } else if (maptype == 1) {
            sa.setChecked(true);
        } else if (maptype == 2) {
            t.setChecked(true);
        } else {
            h.setChecked(true);
        }

        retur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(con, MapsActivity.class);
                i.putExtra("dis", totaldis);
                i.putExtra("start", s);
                i.putParcelableArrayListExtra("locs", locarr);
                i.putExtra("map", maptype);
                i.putExtra("owner", dh.getOwnerHelper(owner));
                i.putExtra("bundle",lisbun);
                startActivity(i);
            }
        });
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.radioButton_normal:
                if (checked) {
                    maptype = 0;
                }
                break;
            case R.id.radioButton_hybrid:
                if (checked) {
                    maptype = 3;
                }
                break;
            case R.id.radioButton_terrain:
                if (checked) {
                    maptype = 2;
                }
                break;
            case R.id.radioButton_satellite:
                if (checked) {
                    maptype = 1;
                }
                break;
        }
        Log.d("maptype", Integer.toString(maptype));
    }
}
