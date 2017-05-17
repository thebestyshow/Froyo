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
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StartWalk extends AppCompatActivity {
    private DatabaseHandler dh = new DatabaseHandler(this);
    private Owner owner;
    private String[] dogs = {};
    private List<Integer> selected = new ArrayList<Integer>();
    private List<Dog> list = new ArrayList<>();
    private StringBuilder sb = new StringBuilder();
    private TextView dogs_tv;
    private Button start;
    private ListView listView;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Start Walk");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_walk);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dogs_tv = (TextView) findViewById(R.id.Dog_list_tv);
        final Context con = this;
        start = (Button) findViewById(R.id.button_start);
        owner = (Owner) getIntent().getSerializableExtra("owner");
        list = dh.getAllDogs(owner.getId());

        for (Dog d : list) {
            dogs = Arrays.copyOf(dogs, dogs.length + 1);
            dogs[dogs.length - 1] = "Name: " + d.getName();
        }

        //setup the adapter to dissplay the dogs
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dogs);
        listView = (ListView) findViewById(R.id.lv_ownerdogs);
        listView.setAdapter(adapter);

        //when a dog is clicked, add the dog to the string
        //if the dog is already in the string, remove it
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (selected.contains(position)) {
                    selected.remove(Integer.valueOf(position));
                    if (selected.isEmpty()) {
                        dogs_tv.setText("");
                        return;
                    }

                    for (int i : selected) {
                        if (i == selected.get(selected.size() - 1))
                            sb.append(list.get(i).getName());
                        else {
                            sb.append(list.get(i).getName() + ", ");
                        }
                        dogs_tv.setText(sb.toString());
                        sb.setLength(0);
                    }
                } else {
                    selected.add(position);
                    for (int i : selected) {
                        if (i == selected.get(selected.size() - 1))
                            sb.append(list.get(i).getName());
                        else {
                            sb.append(list.get(i).getName() + ", ");
                        }
                    }
                    dogs_tv.setText(sb.toString());
                    sb.setLength(0);
                }
            }
        });

        //when start is pressed pass in the dogs and pre set the map to type 0
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(con, MapsActivity.class);
                ArrayList<Dog> passList = new ArrayList<Dog>();

                for (int num : selected) {
                    passList.add(list.get(num));
                }

                Bundle lisbun = new Bundle();
                lisbun.putSerializable("ARRAYLIST", passList);
                i.putExtra("bundle", lisbun);
                i.putExtra("map", 0);
                i.putExtra("owner", dh.getOwnerHelper(owner));
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
