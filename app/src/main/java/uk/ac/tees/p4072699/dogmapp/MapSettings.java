package uk.ac.tees.p4072699.dogmapp;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import java.util.ArrayList;

public class MapSettings extends AppCompatActivity {
    private Owner owner;
    private int maptype;
    private Bundle lisbun;
    private DatabaseHandler dh = new DatabaseHandler(this);
    private Double totaldis;
    private String s;
    private ArrayList<Location> locarr = new ArrayList<Location>();
    private Button retur, save;
    private RadioButton n, t, sa, h;
    private boolean checked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Map Settings");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Context con = this;
        retur = (Button) findViewById(R.id.button_return);
        save = (Button) findViewById(R.id.button_save2);

        //get all of the items that have being passed from the map screen
        owner = (Owner) getIntent().getSerializableExtra("owner");
        lisbun = getIntent().getExtras().getBundle("bundle");
        totaldis = getIntent().getDoubleExtra("dis", 0);
        s = getIntent().getStringExtra("start");
        if (!getIntent().getParcelableArrayListExtra("locs").isEmpty()) {
            locarr = getIntent().getParcelableArrayListExtra("locs");
        }
        maptype = getIntent().getIntExtra("mt", 0);

        //setup the radio buttons
        n = (RadioButton) findViewById(R.id.radioButton_normal);
        t = (RadioButton) findViewById(R.id.radioButton_terrain);
        sa = (RadioButton) findViewById(R.id.radioButton_satellite);
        h = (RadioButton) findViewById(R.id.radioButton_hybrid);

        //highlight the radio button that is currently used
        if (maptype == 0) {
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

        //pass all the details back to the map including maptype
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(con, MapsActivity.class);
                i.putExtra("dis", totaldis);
                i.putExtra("start", s);
                i.putParcelableArrayListExtra("locs", locarr);
                i.putExtra("map", maptype);
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

    //get the radio button that has been selected
    public void onRadioButtonClicked(View view) {
       checked = ((RadioButton) view).isChecked();

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
