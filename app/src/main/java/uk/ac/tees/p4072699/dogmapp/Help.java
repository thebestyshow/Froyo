package uk.ac.tees.p4072699.dogmapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;

public class Help extends AppCompatActivity {
    private Owner owner;
    private DatabaseHandler dh = new DatabaseHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Help");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Context con = this;
        owner = (Owner) getIntent().getSerializableExtra("owner");
        final ImageButton set = (ImageButton) findViewById(R.id.imageButton_settings);
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
