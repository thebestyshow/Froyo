package uk.ac.tees.p4072699.dogmapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

public class MapSettings extends AppCompatActivity {
    Owner owner;
    int maptype;
    DatabaseHandler dh = new DatabaseHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final Context con = this;
        final Button retur = (Button) findViewById(R.id.button_return);
        final Button save = (Button) findViewById(R.id.button_savez);
        owner = (Owner) getIntent().getSerializableExtra("owner");

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
                i.putExtra("map", maptype);
                i.putExtra("owner", dh.getOwnerHelper(owner));
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
            case R.id.radioButton_hybrid:
                if (checked) {
                    maptype = 3;
                }
            case R.id.radioButton_terrain:
                if (checked) {
                    maptype = 2;
                }
            case R.id.radioButton_satellite:
                if (checked) {
                    maptype = 1;
                }
        }
    }
}
