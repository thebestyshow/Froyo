package uk.ac.tees.p4072699.dogmapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Settings");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings2);
//        final Button retur = (Button) findViewById(R.id.button_return);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        retur.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
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
