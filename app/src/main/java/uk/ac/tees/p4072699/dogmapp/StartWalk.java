package uk.ac.tees.p4072699.dogmapp;

import android.content.Context;
import android.content.Intent;
import android.renderscript.RenderScript;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

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
    StringBuilder sb = new StringBuilder();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Start Walk");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_walk);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final TextView dogs_tv = (TextView) findViewById(R.id.Dog_list_tv);
        final Context con = this;
//        final Button cancel = (Button) findViewById(R.id.button_cancel);
        final Button start = (Button) findViewById(R.id.button_start);
        owner = (Owner) getIntent().getSerializableExtra("owner");
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

                    if (selected.isEmpty()){
                        dogs_tv.setText("");
                        return;
                    }
                    for (int i : selected){
                        if (i == selected.get(selected.size()-1))
                            sb.append(list.get(i).getName());
                        else{
                            sb.append(list.get(i).getName() + ", ");
                        }
                        dogs_tv.setText(sb.toString());
                        sb.setLength(0);
                    }
                } else {
                    selected.add(position);
                    for (int i : selected){
                        if (i == selected.get(selected.size()-1))
                            sb.append(list.get(i).getName());
                        else{
                            sb.append(list.get(i).getName() + ", ");
                        }
                    }
                    dogs_tv.setText(sb.toString());
                    sb.setLength(0);
                }

            }
        });


//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(con, Home.class);
//                i.putExtra("owner", dh.getOwnerHelper(owner));
//                startActivity(i);
//            }
//        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(con, MapsActivity.class);
                ArrayList<Dog> passList = new ArrayList<Dog>();

                for (int num : selected){
                    //list.get(num).setTotwalks(list.get(num).getTotwalks() + 1);
                    passList.add(list.get(num));
                }

                Bundle lisbun = new Bundle();
                lisbun.putSerializable("ARRAYLIST",passList);
                i.putExtra("bundle",lisbun);
                i.putExtra("map", 0);
                i.putExtra("owner", dh.getOwnerHelper(owner));
                Calendar c = new GregorianCalendar();
                String s = c.getTime().toString();
                i.putExtra("start", s);
                startActivity(i);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return  true;
        }
        return super.onOptionsItemSelected(item);

    }
}
