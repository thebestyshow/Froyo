package uk.ac.tees.p4072699.dogmapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

public class ReviewList extends AppCompatActivity {
    DatabaseHandler dh = new DatabaseHandler(this);
    Owner owner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list);

        final Context con = this;
        final Button home = (Button) findViewById(R.id.button_home);
        owner = (Owner) getIntent().getSerializableExtra("owner");


        String[] reviews = {};

        List<Walk> list = dh.getAllWalks();

        for (Walk w : list) {
            reviews = Arrays.copyOf(reviews, reviews.length + 1);
            reviews[reviews.length - 1] = "Name: " + w.getName() +"\nRating: " + w.getRating() + "\nComment: " + w.getComment();
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, reviews);

        ListView listView = (ListView) findViewById(R.id.lv_rev);
        listView.setAdapter(adapter);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(con,Home.class);
                i.putExtra("owner",owner);
                startActivity(i);
            }
        });
    }
}
