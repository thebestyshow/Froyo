package uk.ac.tees.p4072699.dogmapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class DogProfile extends AppCompatActivity {
    private DatabaseHandler dh = new DatabaseHandler(this);
    private Dog d;
    private Owner owner;
    private int selected = -1;

    //* this displays the dog profile. This will have the name, total wlaks, average distance and also
    //* the total distance. There is also the option to remove and edit the dog on the screen with buttons

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Dog Profile");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        owner = (Owner) getIntent().getSerializableExtra("owner");
        d = (Dog) getIntent().getSerializableExtra("dog");
        final TextView name = (TextView) findViewById(R.id.textView_dogname);
        final TextView totWalks = (TextView) findViewById(R.id.dog_tot_walks);
        final TextView avgWalks = (TextView) findViewById(R.id.dog_avg_dis);
        final TextView totDis = (TextView) findViewById(R.id.dog_tot_dis);

        final Context con = getApplicationContext();
        final Button rem = (Button) findViewById(R.id.button_remove);
        final Button ed = (Button) findViewById(R.id.button_savez);

        name.setText(d.getName());
        DecimalFormat df = new DecimalFormat("00.00");
        String avg = df.format(d.getTotdistance() / d.getTotwalks());

        totWalks.setText(String.valueOf(d.getTotwalks()));
        totDis.setText(df.format(d.getTotdistance()) + "km");
        avgWalks.setText(avg + "km");

        rem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selected == -1) {
                    Toast.makeText(getApplicationContext(), "Choose a dog to remove", Toast.LENGTH_SHORT).show();
                } else {
                    dh.removeDog(selected);
                    Intent intent = new Intent(con, DogList.class);
                    intent.putExtra("owner", dh.getOwnerHelper(owner));
                    startActivity(intent);
                }
            }
        });

        ed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(con, EditDog.class);
                i.putExtra("owner", dh.getOwnerHelper(owner));
                i.putExtra("dog", d);
                startActivity(i);
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent i = new Intent(getApplicationContext(), DogList.class);
            i.putExtra("owner", owner);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

