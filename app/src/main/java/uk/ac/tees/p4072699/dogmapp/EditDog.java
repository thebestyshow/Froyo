package uk.ac.tees.p4072699.dogmapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditDog extends AppCompatActivity {
    private DatabaseHandler dh = new DatabaseHandler(this);
    private Dog d;
    private Owner owner;
    private Button save;
    private EditText dgname;

    //* this class will send you to the edit dog screen so the user can change the details of
    //* the dog if they have mis-spelt the name for example. You can also remove and save the entry
    //* if the home button is pressed, you will be sent back to the home screen
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Add Dog");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dog);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        owner = (Owner) getIntent().getSerializableExtra("owner");
        final Context con = this;

        save = (Button) findViewById(R.id.button_save2);
        dgname = (EditText) findViewById(R.id.editText_dgname);

        final Button save = (Button) findViewById(R.id.button_savez);
        final EditText dgname = (EditText) findViewById(R.id.editText_dgname);


        owner = (Owner) getIntent().getSerializableExtra("owner");
        d = (Dog) getIntent().getSerializableExtra("dog");

        dgname.setText(d.getName());


        save.setOnClickListener(new View.OnClickListener() {

        save.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                dh.edit(d, dgname.getText().toString());
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
