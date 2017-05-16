package uk.ac.tees.p4072699.dogmapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class AddDogActivity extends AppCompatActivity {
    DatabaseHandler dh = new DatabaseHandler(this);

    Owner owner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Add Dog");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dog);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        owner = (Owner) getIntent().getSerializableExtra("owner");

        final Context con = this;
//        final Button cancel = (Button) findViewById(R.id.button_cancel);
        final Button save = (Button) findViewById(R.id.button_save);
        final EditText dgname = (EditText) findViewById(R.id.editText_dgname);

        owner = (Owner) getIntent().getSerializableExtra("owner");


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dh.add(new Dog(dh.getDogCount() + 1, dgname.getText().toString(), owner.getId()));
                Intent intent = new Intent(con, DogList.class);
                intent.putExtra("owner", dh.getOwnerHelper(owner));
                startActivity(intent);
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
}