package uk.ac.tees.p4072699.dogmapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class Help extends AppCompatActivity {
    Button home;
    Owner owner;
    DatabaseHandler dh = new DatabaseHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Help");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Context con = this;
        owner = (Owner) getIntent().getSerializableExtra("owner");
//        home = (Button) findViewById(R.id.help_btn_home);
        final ImageButton set = (ImageButton) findViewById(R.id.imageButton_settings);


//        home.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(getApplicationContext(), Home.class);
//                i.putExtra("owner", dh.getOwnerHelper(owner));
//                startActivity(i);
//                setContentView(R.layout.activity_home);
//                finish();
//            }
//        });
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return  true;
        }
        return super.onOptionsItemSelected(item);

    }
}
