package uk.ac.tees.p4072699.dogmapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

public class Profile extends AppCompatActivity {
    private DatabaseHandler dh = new DatabaseHandler(this);
    private String[] dogs = {};
    private Integer[] dogsId = {};
    private Owner owner;
    private TextView name, totWalks, avgWalks, totDis;
    private ListView lv;

    /*Initialises all TextView and buttons. Constructs an ArrayAdapter to populate the ListView with data. Displays the users stats, such as total walks
    * and total distance.
    * If Edit Profile button is pressed then the user is taken to the edit profile activity. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Profile");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        owner = (Owner) getIntent().getSerializableExtra("owner");
        name = (TextView) findViewById(R.id.Prof_name);
        totWalks = (TextView) findViewById(R.id.prof_totwalks);
        avgWalks = (TextView) findViewById(R.id.Prof_avgwalks);
        totDis = (TextView) findViewById(R.id.prof_tot_dis);
        final Context con = this;

        totWalks.setText(Integer.toString(owner.getTot_walks()));
        name.setText(owner.getName());
        DecimalFormat df = new DecimalFormat("00.00");
        String avg = df.format(owner.getTot_dis() / owner.getTot_walks());
        avgWalks.setText(avg + "KM");
        totDis.setText(df.format(owner.getTot_dis()));
        final Button editProf = (Button) findViewById(R.id.button_editProf);
        List<Dog> list = dh.getAllDogs(owner.getId());

        for (Dog dg : list) {
            dogs = Arrays.copyOf(dogs, dogs.length + 1);

            Cursor cID = dh.getReadableDatabase().rawQuery("SELECT * FROM " + dh.getOwnerLogintable()
                    + " WHERE " + dh.getColId() + "=?", new String[]{Integer.toString(dg.getOwnerID())});

            dogs[dogs.length - 1] = dg.getName();
            dogsId = Arrays.copyOf(dogsId, dogsId.length + 1);
            dogsId[dogsId.length - 1] = dg.getId();
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dogs);

        lv = (ListView) findViewById(R.id.Prof_dogList);
        lv.setAdapter(adapter);

        editProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(con, EditProfile.class);
                i.putExtra("owner", owner);
                startActivity(i);
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
