package uk.ac.tees.p4072699.dogmapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.List;

public class Profile extends AppCompatActivity {
    DatabaseHandler dh = new DatabaseHandler(this);
    String[] dogs = {};
    Integer[] dogsId = {};
    Owner owner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        owner = (Owner) getIntent().getSerializableExtra("owner");

        final TextView name = (TextView) findViewById(R.id.Prof_name);
        final Context con = this;

        name.setText(owner.getName());

        final Button home = (Button) findViewById(R.id.button_home);
        List<Dog> list = dh.getAllDogs(owner.getId());

        for (Dog dg : list) {
            dogs = Arrays.copyOf(dogs, dogs.length + 1);

            Cursor cID = dh.getReadableDatabase().rawQuery("SELECT * FROM " + dh.getOwnerLogintable()
                    + " WHERE " + dh.getColId() + "=?",new String[]{Integer.toString(dg.getOwnerID())});

            dogs[dogs.length - 1] = "Name: " + dg.getName() +"\nOwner: " + dh.getOneOwner(cID).getName();
            dogsId = Arrays.copyOf(dogsId, dogsId.length + 1);
            dogsId[dogsId.length - 1] = dg.getId();
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dogs);

        ListView lv = (ListView) findViewById(R.id.Prof_dogList);
        lv.setAdapter(adapter);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(con, Home.class);
                intent.putExtra("owner", owner);
                startActivity(intent);
                setContentView(R.layout.activity_home);
            }
        });
    }
}
