package uk.ac.tees.p4072699.dogmapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReviewList extends AppCompatActivity {
    private DatabaseHandler dh = new DatabaseHandler(this);
    private Owner owner;
    private int selected;
    private String[] reviews = {};
    private Integer[] revId = {};
    private Button home;
    private ListView listView;
    private ArrayAdapter adapter;

    /*Sets the title, the Up navigation, the home button, and the Walk list.
    * Contains the logic for the walk display and list click actions.*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Reviews");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Context con = this;
        home = (Button) findViewById(R.id.Rev_home);
        owner = (Owner) getIntent().getSerializableExtra("owner");

        List<Walk> list = new ArrayList<>();
        try {
            list = dh.getAllWalks();
            Log.d("WALK ARRAY", list.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (Walk w : list) {
            reviews = Arrays.copyOf(reviews, reviews.length + 1);

            reviews[reviews.length - 1] = "Name: " + w.getName() + "\nRating: " + w.getRating() + "\nComment: " + w.getComment();
            revId = Arrays.copyOf(revId, revId.length + 1);
            revId[revId.length - 1] = w.getId();
        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, reviews);

        for (Walk w : list) {
            Log.d("LIST CHECK", w.getId() + " : " + w.getPoints().toString());
        }

        listView = (ListView) findViewById(R.id.lv_rev);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected = revId[position];
                List<Walk> wlist = null;
                try {
                    wlist = dh.getAllWalks();
                    Log.d("DATABASE", wlist.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("list check", wlist.toString());
                Intent i = new Intent(con, ReviewView.class);
                Log.d("SELECTED WALK", wlist.get(position).getName());
                Log.d("SELECTED ARRAY", wlist.get(position).getPoints().toString());
                i.putParcelableArrayListExtra("pointsarray", wlist.get(position).getPoints());
                wlist.get(position).setPoints(null);
                i.putExtra("revlist","revlist");
                i.putExtra("walk", wlist.get(position));
                i.putExtra("owner", owner);
                startActivity(i);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(con, Home.class);
                intent.putExtra("owner", dh.getOwnerHelper(owner));
                startActivity(intent);
            }
        });
    }

    /*checks if a menu item is pressed and if it is, the user is returned to the previous screen */
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent i = new Intent(getApplicationContext(), Home.class);
            i.putExtra("owner", owner);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
