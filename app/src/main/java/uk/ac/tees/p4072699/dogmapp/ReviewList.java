package uk.ac.tees.p4072699.dogmapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

public class ReviewList extends AppCompatActivity {
    DatabaseHandler dh = new DatabaseHandler(this);
    Owner owner;
    int selected;
    String[] reviews = {};
    Integer[] revId = {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list);

        final Context con = this;
        final Button home = (Button) findViewById(R.id.button_home);
        final Button rem = (Button) findViewById(R.id.button_removerev);
        owner = (Owner) getIntent().getSerializableExtra("owner");
        final ImageButton set = (ImageButton) findViewById(R.id.imageButton_settings);

        List<Walk> list = dh.getAllWalks();

        for (Walk w : list) {
            reviews = Arrays.copyOf(reviews, reviews.length + 1);
            reviews[reviews.length - 1] = "Name: " + w.getName() + "\nRating: " + w.getRating() + "\nComment: " + w.getComment();
            revId = Arrays.copyOf(revId, revId.length + 1);
            revId[revId.length - 1] = w.getId();
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, reviews);

        ListView listView = (ListView) findViewById(R.id.lv_rev);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected = revId[position];
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(con, Home.class);
                i.putExtra("owner", dh.getOwnerHelper(owner));
                startActivity(i);
            }
        });

        rem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dh.removeWalk(selected);
                Intent intent = new Intent(con, ReviewList.class);
                intent.putExtra("owner", dh.getOwnerHelper(owner));
                startActivity(intent);
            }
        });

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(con, Settings.class);
                i.putExtra("owner", dh.getOwnerHelper(owner));
                startActivity(i);
            }
        });

    }
}
