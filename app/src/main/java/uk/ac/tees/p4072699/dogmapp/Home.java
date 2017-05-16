package uk.ac.tees.p4072699.dogmapp;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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

import java.util.Arrays;
import java.util.List;

public class Home extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    DatabaseHandler dh = new DatabaseHandler(this);
    Owner owner;
    Walk w;
    int selected;
    String[] walks = {};
    Integer[] walkID = {};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView mNavigationView = (NavigationView) findViewById(R.id.nav_menu);

        List<Walk> list = null;
        try {
            list = dh.getAllWalks();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 10; i++) {
            if (i > walks.length - 1) {
                break;
            } else {
                walks = Arrays.copyOf(walks, walks.length + 1);
                walks[walks.length - 1] = "Name: " + list.get(i).getName() + "\nRating: " + list.get(i).getRating() + "\nComment: " + list.get(i).getComment();
                walkID = Arrays.copyOf(walkID, walkID.length + 1);
                walkID[walkID.length - 1] = list.get(i).getId();
            }

        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, walks);

        ListView walkList = (ListView) findViewById(R.id.lv_walks);
        walkList.setAdapter(adapter);

        final Button start = (Button) findViewById(R.id.button_startw);

        owner = (Owner) getIntent().getSerializableExtra("owner");

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case (R.id.nav_account):

                        Intent accountActivity = new Intent(getApplicationContext(), Profile.class);
                        accountActivity.putExtra("owner", dh.getOwnerHelper(owner));
                        startActivity(accountActivity);
                        break;
                    case (R.id.nav_start_walk):
                        accountActivity = new Intent(getApplicationContext(), StartWalk.class);
                        accountActivity.putExtra("owner", dh.getOwnerHelper(owner));
                        startActivity(accountActivity);
                        break;
                    case (R.id.nav_dogs):
                        accountActivity = new Intent(getApplicationContext(), DogList.class);
                        accountActivity.putExtra("owner", dh.getOwnerHelper(owner));
                        startActivity(accountActivity);
                        break;
                    case (R.id.nav_reviews):
                        accountActivity = new Intent(getApplicationContext(), ReviewList.class);
                        accountActivity.putExtra("owner", dh.getOwnerHelper(owner));
                        startActivity(accountActivity);
                        break;
                    case (R.id.nav_weather):
                        accountActivity = new Intent(getApplicationContext(), Weather.class);
                        accountActivity.putExtra("owner", dh.getOwnerHelper(owner));
                        startActivity(accountActivity);
                        break;
                    case (R.id.nav_settings):
                        accountActivity = new Intent(getApplicationContext(), Settings.class);
                        accountActivity.putExtra("owner", dh.getOwnerHelper(owner));
                        startActivity(accountActivity);
                        break;
                    case (R.id.nav_help):
                        accountActivity = new Intent(getApplicationContext(), Help.class);
                        accountActivity.putExtra("owner", dh.getOwnerHelper(owner));
                        startActivity(accountActivity);
                        break;
                    case (R.id.nav_logout):
                        accountActivity = new Intent(getApplicationContext(), Login.class);
                        startActivity(accountActivity);
                        break;
                }
                return true;
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), StartWalk.class);
                i.putExtra("owner", dh.getOwnerHelper(owner));
                startActivity(i);
            }
        });

        walkList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected = walkID[position];
                List<Walk> wlist = null;
                try {
                    wlist = dh.getAllWalks();
                    Log.d("DATABASE", wlist.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent i = new Intent(getApplicationContext(), ReviewView.class);
                Log.d("SELECTED WALK", wlist.get(position).getName());
                Log.d("SELECTED ARRAY", wlist.get(position).getPoints().toString());
                i.putParcelableArrayListExtra("pointsarray", wlist.get(position).getPoints());
                wlist.get(position).setPoints(null);
                i.putExtra("walk", wlist.get(position));
                i.putExtra("owner", owner);
                startActivity(i);
            }
        });


    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;

        }
        return super.onOptionsItemSelected(item);

    }


}
