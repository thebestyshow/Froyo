package uk.ac.tees.p4072699.dogmapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class DogList extends AppCompatActivity {
    DatabaseHandler dh = new DatabaseHandler(this);
    String selected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_list);

        final Context con = this;
        final Button home = (Button) findViewById(R.id.button_home);
        final Button add = (Button) findViewById(R.id.button_add);

        String[] dogs = {};

        List<Dog> list = dh.getAllDogs();

        for (Dog dg : list) {
            String log = "Dog Name:" + dg.getName() +" Owner: " + dg.getOwner();
            dogs = Arrays.copyOf(dogs, dogs.length + 1);
            dogs[dogs.length - 1] = "Dog Name: " + dg.getName() +" Owner: " + dg.getOwner();
            Log.d("Database", log);
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dogs);

        ListView listView = (ListView) findViewById(R.id.lv_dgs);
        listView.setAdapter(adapter);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(con, HomeActivity.class);
                startActivity(intent);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(con, AddDogActivity.class);
                startActivity(intent);
            }
        });
    }
}