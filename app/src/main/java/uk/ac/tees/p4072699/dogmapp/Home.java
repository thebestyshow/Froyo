package uk.ac.tees.p4072699.dogmapp;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class Home extends AppCompatActivity {
    Owner owner;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    DatabaseHandler dh = new DatabaseHandler(this);


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



//        switch (MenuItem.getItemId()){
//            case (R.id.nv1):
//                Intent in = new Intent(getApplicationContext(), Profile.class);
//                startActivity(in);
//                break;
//            case (R.id.ITEM2_ID):
//                in = new Intent(getApplicationContext(), ACTIVITY2_NAME.class);
//                startActivity(in);
//        }

//        final Context con = this;
//        final Button add = (Button) findViewById(R.id.button_doglist);
//        final Button help = (Button) findViewById(R.id.home_btn_help);
//        final Button start = (Button) findViewById(R.id.button_start);
//        final Button rev = (Button) findViewById(R.id.button_reviews);
//        final Button prof = (Button) findViewById(R.id.button_profile);
//        final Button logout = (Button) findViewById(R.id.button_logout);
//        final Button weather = (Button) findViewById(R.id.button_weather);
//        final ImageButton set = (ImageButton) findViewById(R.id.imageButton_settings);
        final TextView name = (TextView) findViewById(R.id.Name_test);

        owner = (Owner) getIntent().getSerializableExtra("owner");
        name.setText(owner.getName());

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem){
                switch (menuItem.getItemId()){
                    case(R.id.nav_account):

                        Intent accountActivity = new Intent(getApplicationContext(), Profile.class);
                        accountActivity.putExtra("owner", dh.getOwnerHelper(owner));
                        startActivity(accountActivity);
                        break;
                    case(R.id.nav_start_walk):
                        accountActivity = new Intent(getApplicationContext(), StartWalk.class);
                        accountActivity.putExtra("owner", dh.getOwnerHelper(owner));
                        startActivity(accountActivity);
                        break;
                    case(R.id.nav_dogs):
                        accountActivity = new Intent(getApplicationContext(), DogList.class);
                        accountActivity.putExtra("owner", dh.getOwnerHelper(owner));
                        startActivity(accountActivity);
                        break;
                    case(R.id.nav_reviews):
                        accountActivity = new Intent(getApplicationContext(), ReviewList.class);
                        accountActivity.putExtra("owner", dh.getOwnerHelper(owner));
                        startActivity(accountActivity);
                        break;
                    case(R.id.nav_weather):
                        accountActivity = new Intent(getApplicationContext(), Weather.class);
                        accountActivity.putExtra("owner", dh.getOwnerHelper(owner));
                        startActivity(accountActivity);
                        break;
                    case(R.id.nav_settings):
                        accountActivity = new Intent(getApplicationContext(), Settings.class);
                        accountActivity.putExtra("owner", dh.getOwnerHelper(owner));
                        startActivity(accountActivity);
                        break;
                    case(R.id.nav_help):
                        accountActivity = new Intent(getApplicationContext(), Help.class);
                        accountActivity.putExtra("owner", dh.getOwnerHelper(owner));
                        startActivity(accountActivity);
                        break;
                    case(R.id.nav_logout):
                        accountActivity = new Intent(getApplicationContext(), Login.class);
                        startActivity(accountActivity);
                        break;
                }
                return true;
            }
        });


//        weather.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(con, Weather.class);
//                i.putExtra("owner", dh.getOwnerHelper(owner));
//                startActivity(i);
//            }
//        });
//
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(con, Login.class);
//                startActivity(i);
//            }
//        });
//
//        set.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(con, Settings.class);
//                i.putExtra("owner", dh.getOwnerHelper(owner));
//                startActivity(i);
//            }
//        });
//
//        help.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(con, Help.class);
//                i.putExtra("owner", dh.getOwnerHelper(owner));
//                startActivity(i);
//            }
//        });
//
//        start.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(con, StartWalk.class);
//                i.putExtra("owner", dh.getOwnerHelper(owner));
//                startActivity(i);
//            }
//        });
//
//        rev.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(con, ReviewList.class);
//                i.putExtra("owner", dh.getOwnerHelper(owner));
//                startActivity(i);
//            }
//        });
//
//        add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(con, DogList.class);
//                i.putExtra("owner", dh.getOwnerHelper(owner));
//                startActivity(i);
//                setContentView(R.layout.activity_dog_list);
//            }
//        });
//
//        prof.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(con, Profile.class);
//                i.putExtra("owner", dh.getOwnerHelper(owner));
//                startActivity(i);
//                setContentView(R.layout.activity_profile);
//
//            }
//        });


    }
    public boolean onOptionsItemSelected(MenuItem item){
        if(mToggle.onOptionsItemSelected(item)){
            return true;

        }
        return super.onOptionsItemSelected(item);

    }


}
