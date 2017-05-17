package uk.ac.tees.p4072699.dogmapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

public class DogList extends AppCompatActivity {
    private DatabaseHandler dh = new DatabaseHandler(this);
    private int selected = -1;
    private String[] dogs = {};
    private Integer[] dogsId = {};
    private Owner owner;

    /*Initialises all buttons and the ListView. An ArrayAdapter is created to populate the list. If the user selects an item on the list,
    * they are taken to Dogs profile activity. If the user pressed the add button, they will be taken to the add dog activity*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Dogs");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Context con = this;
        final Button add = (Button) findViewById(R.id.button_add);
        final ListView listView = (ListView) findViewById(R.id.lv_dgs);
        owner = (Owner) getIntent().getSerializableExtra("owner");
        DecimalFormat df = new DecimalFormat("#.00");
        List<Dog> list = dh.getAllDogs(owner.getId());

        for (Dog dg : list) {
            dogs = Arrays.copyOf(dogs, dogs.length + 1);

            dogs[dogs.length - 1] = "Name: " + dg.getName();

            dogsId = Arrays.copyOf(dogsId, dogsId.length + 1);
            dogsId[dogsId.length - 1] = dg.getId();
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dogs);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected = dogsId[position];
                List<Dog> list = dh.getAllDogs(owner.getId());
                Intent i = new Intent(con, DogProfile.class);
                i.putExtra("dog", list.get(position));
                i.putExtra("owner", dh.getOwnerHelper(owner));
                startActivity(i);
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(con, AddDogActivity.class);
                intent.putExtra("owner", dh.getOwnerHelper(owner));
                startActivity(intent);
            }
        });
    }

    /*checks if a menu item is pressed and if it is, the user is taken the home screen */
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