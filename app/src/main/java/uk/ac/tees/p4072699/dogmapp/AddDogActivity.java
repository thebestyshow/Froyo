package uk.ac.tees.p4072699.dogmapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddDogActivity extends AppCompatActivity {

    DatabaseHandler dh = new DatabaseHandler(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dog);

        final Context con = this;
        final Button cancel = (Button) findViewById(R.id.button_cancel);
        final Button save = (Button) findViewById(R.id.button_save);
        final EditText dgname = (EditText) findViewById(R.id.editText_dgname);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(con, DogList.class);
                startActivity(intent);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dh.add(new Dog(dgname.getText().toString(), "Mike"));
                Intent intent = new Intent(con, DogList.class);
                startActivity(intent);
            }
        });
    }
}