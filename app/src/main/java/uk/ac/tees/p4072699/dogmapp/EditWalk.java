package uk.ac.tees.p4072699.dogmapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class EditWalk extends AppCompatActivity {
    private DatabaseHandler dh = new DatabaseHandler(this);
    private Walk w;
    private Owner owner;
    private int rating;
    private String name;
    private ImageButton p1, p2, p3, p4, p5;
    private EditText etname, etcomm;
    private TextView tv_dis, tv_time;
    private Button retur, save;

    /*Sets the title, the points arraylist, the EditText fields for name,
    * distance, comments, and time, asa well as the buttons for return and save.
    * Paw imagebuttons are setup for the rating system, and a Decimal
    * format for the distance covered.*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Edit Walk");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_walk);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        owner = (Owner) getIntent().getSerializableExtra("owner");
        w = (Walk) getIntent().getSerializableExtra("walk");
        rating = w.getRating();
        ArrayList<LatLng> tempP = getIntent().getParcelableArrayListExtra("pointsarray");
        w.setPoints(tempP);
        etname = (EditText) findViewById(R.id.et_name);
        etcomm = (EditText) findViewById(R.id.et_comm);
        tv_dis = (TextView) findViewById(R.id.tv_distance);
        tv_time = (TextView) findViewById(R.id.tv_time);
        retur = (Button) findViewById(R.id.button_return);
        save = (Button) findViewById(R.id.button_save2);
        p1 = (ImageButton) findViewById(R.id.paw_1);
        p2 = (ImageButton) findViewById(R.id.paw_2);
        p3 = (ImageButton) findViewById(R.id.paw_3);
        p4 = (ImageButton) findViewById(R.id.paw_4);
        p5 = (ImageButton) findViewById(R.id.paw_5);
        DecimalFormat df = new DecimalFormat("#.00");

        etname.setText(w.getName());
        etcomm.setText(w.getComment());
        tv_dis.setText(df.format(w.getLength()));
        tv_time.setText(String.valueOf(w.getTime()));

        /*Paw rating logic for what happens when the user selects a certain rating.
        * E.g. if rating is 2 then call the selected paw drawable for paw 1 and 2.*/
        if (w.getRating() == 1) {
            p1.setImageResource(R.drawable.selected);
            p2.setImageResource(R.drawable.paw);
            p3.setImageResource(R.drawable.paw);
            p4.setImageResource(R.drawable.paw);
            p5.setImageResource(R.drawable.paw);
        } else if (w.getRating() == 2) {
            p2.setImageResource(R.drawable.selected);
            p1.setImageResource(R.drawable.selected);
            p3.setImageResource(R.drawable.paw);
            p4.setImageResource(R.drawable.paw);
            p5.setImageResource(R.drawable.paw);
        } else if (w.getRating() == 3) {
            p3.setImageResource(R.drawable.selected);
            p2.setImageResource(R.drawable.selected);
            p1.setImageResource(R.drawable.selected);
            p4.setImageResource(R.drawable.paw);
            p5.setImageResource(R.drawable.paw);
        } else if (w.getRating() == 4) {
            p4.setImageResource(R.drawable.selected);
            p2.setImageResource(R.drawable.selected);
            p3.setImageResource(R.drawable.selected);
            p1.setImageResource(R.drawable.selected);
            p5.setImageResource(R.drawable.paw);
        } else if (w.getRating() == 5) {
            p5.setImageResource(R.drawable.selected);
            p2.setImageResource(R.drawable.selected);
            p3.setImageResource(R.drawable.selected);
            p4.setImageResource(R.drawable.selected);
            p1.setImageResource(R.drawable.selected);
        }

        /*Click listeners for the paws, rating value is changed when
        * the user clicks on a certain paw.*/
        p1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                rating = 1;
                p1.setImageResource(R.drawable.selected);
                p2.setImageResource(R.drawable.paw);
                p3.setImageResource(R.drawable.paw);
                p4.setImageResource(R.drawable.paw);
                p5.setImageResource(R.drawable.paw);
            }
        });

        p2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                rating = 2;
                p2.setImageResource(R.drawable.selected);
                p1.setImageResource(R.drawable.selected);
                p3.setImageResource(R.drawable.paw);
                p4.setImageResource(R.drawable.paw);
                p5.setImageResource(R.drawable.paw);
            }
        });

        p3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                rating = 3;
                p3.setImageResource(R.drawable.selected);
                p2.setImageResource(R.drawable.selected);
                p1.setImageResource(R.drawable.selected);
                p4.setImageResource(R.drawable.paw);
                p5.setImageResource(R.drawable.paw);

            }
        });

        p4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                rating = 4;
                p4.setImageResource(R.drawable.selected);
                p2.setImageResource(R.drawable.selected);
                p3.setImageResource(R.drawable.selected);
                p1.setImageResource(R.drawable.selected);
                p5.setImageResource(R.drawable.paw);
            }
        });

        p5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                rating = 5;
                p5.setImageResource(R.drawable.selected);
                p2.setImageResource(R.drawable.selected);
                p3.setImageResource(R.drawable.selected);
                p4.setImageResource(R.drawable.selected);
                p1.setImageResource(R.drawable.selected);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                w.setComment(etcomm.getText().toString());
                w.setName(etname.getText().toString());
                w.setRating(rating);
                dh.editWalk(w);
                Intent i = new Intent(getApplicationContext(), ReviewView.class);
                i.putParcelableArrayListExtra("pointsarray", w.getPoints());
                w.setPoints(null);
                i.putExtra("walk", w);
                i.putExtra("owner", owner);
                startActivity(i);
            }
        });
        retur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    /*checks if a menu item is pressed and if it is, the user is returned to the previous screen */
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
