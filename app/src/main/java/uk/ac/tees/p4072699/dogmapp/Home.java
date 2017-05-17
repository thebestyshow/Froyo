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
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*Sets up the home screen layout, the Navigation Drawer,
* the Database handler, and the walks variables. The home screen
* displays the Start Walk button, as well as the walks that the
* user has taken, the list is displayed when the list size is greater
* than zero.*/
public class Home extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private DatabaseHandler dh = new DatabaseHandler(this);
    private Owner owner;
    private Walk w;
    private int selected;
    private String[] walks = {};
    private Integer[] walkID = {};
    private ListView walkList;
    private TextView doglistlbl;
    private Button start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        /*Navigation Drawer listener and syncState, the Burger navigation icon is enabled here also*/
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView mNavigationView = (NavigationView) findViewById(R.id.nav_menu);
        walkList = (ListView) findViewById(R.id.lv_walks);
        List<Walk> list = new ArrayList<>();
        doglistlbl = (TextView) findViewById(R.id.tv_doglist);

        /*Get the walks and display them in the list.*/
        try {
            list = dh.getAllWalks();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (list.size() > 0){
            walkList.setVisibility(View.VISIBLE);
            doglistlbl.setVisibility(View.VISIBLE);
        }

        for (Walk w :list) {

                walks = Arrays.copyOf(walks, walks.length + 1);
                walks[walks.length - 1] = "Name: " + w.getName() + "\nRating: " + w.getRating() + "\nComment: " + w.getComment();
                walkID = Arrays.copyOf(walkID, walkID.length + 1);
                walkID[walkID.length - 1] = w.getId();
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, walks);
        walkList.setAdapter(adapter);
        start = (Button) findViewById(R.id.button_startw);
        owner = (Owner) getIntent().getSerializableExtra("owner");

        /*Navigation Drawer menu logic*/
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
                    case (R.id.nav_help):
                        accountActivity = new Intent(getApplicationContext(), Help.class);
                        accountActivity.putExtra("owner", dh.getOwnerHelper(owner));
                        startActivity(accountActivity);
                        break;
                    case (R.id.nav_about):
                        accountActivity = new Intent(getApplicationContext(), About.class);
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
                i.putExtra("home","home");
                i.putExtra("walk", wlist.get(position));
                i.putExtra("owner", owner);
                startActivity(i);
            }
        });
    }

    /*checks if a menu item is pressed and if it is, the user is returned to the previous screen */
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
