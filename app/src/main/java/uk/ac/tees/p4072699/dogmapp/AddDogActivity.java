package uk.ac.tees.p4072699.dogmapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddDogActivity extends AppCompatActivity {
    private DatabaseHandler dh = new DatabaseHandler(this);
    private Owner owner;
    private Button save;
    private EditText dgname;

    //*** this here allows the user to add a dog, the dog is then saved to the users name and
    //* this shows the dog belong to the owner
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Add Dog");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dog);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        owner = (Owner) getIntent().getSerializableExtra("owner");
        final Context con = this;
        save = (Button) findViewById(R.id.button_save);
        dgname = (EditText) findViewById(R.id.editText_dgname);
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

    //** if home button is pressed it will return to home
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}