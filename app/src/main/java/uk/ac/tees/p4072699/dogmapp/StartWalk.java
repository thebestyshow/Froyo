package uk.ac.tees.p4072699.dogmapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class StartWalk extends AppCompatActivity {
    DatabaseHandler dh = new DatabaseHandler(this);
    Owner owner;
    String[] dogs = {};
    List<Integer> selected = new ArrayList<Integer>();
    List<Dog> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_walk);

        final Context con = this;
        final Button cancel = (Button) findViewById(R.id.button_cancel);
        final Button start = (Button) findViewById(R.id.button_start);
        owner = (Owner) getIntent().getSerializableExtra("owner");
        final ImageButton set = (ImageButton) findViewById(R.id.imageButton_settings);
        list = dh.getAllDogs(owner.getId());

        for (Dog d : list) {
            dogs = Arrays.copyOf(dogs, dogs.length + 1);
            dogs[dogs.length - 1] = "Name: " + d.getName();
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dogs);

        ListView listView = (ListView) findViewById(R.id.lv_ownerdogs);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (selected.contains(position)) {
                    selected.remove(Integer.valueOf(position));
                } else {
                    selected.add(position);

                }
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

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(con, Home.class);
                i.putExtra("owner", dh.getOwnerHelper(owner));
                startActivity(i);
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(con, MapsActivity.class);
                ArrayList<Dog> passList = new ArrayList<Dog>();

                for (int num : selected){
                    list.get(num).setTotwalks(list.get(num).getTotwalks() + 1);
                    passList.add(list.get(num));
                }

                Bundle lisbun = new Bundle();
                lisbun.putSerializable("ARRAYLIST",(Serializable)passList);
                i.putExtra("bundle",lisbun);

                i.putExtra("owner", dh.getOwnerHelper(owner));
                Calendar c = new GregorianCalendar();
                String s = c.getTime().toString();
                i.putExtra("start", s);
                startActivity(i);
            }
        });
    }
}
