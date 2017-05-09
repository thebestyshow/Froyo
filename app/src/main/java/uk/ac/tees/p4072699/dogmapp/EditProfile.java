package uk.ac.tees.p4072699.dogmapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditProfile extends AppCompatActivity {
    DatabaseHandler dh = new DatabaseHandler(this);
    Owner owner;
    Toast t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        final EditText etname = (EditText) findViewById(R.id.etname);
        final EditText etEmail = (EditText) findViewById(R.id.etEmail);
        final Button changePass = (Button) findViewById(R.id.button_change);
        final Button save = (Button) findViewById(R.id.button_savez);
        final Button retur = (Button) findViewById(R.id.button_retur);

        owner = (Owner) getIntent().getSerializableExtra("owner");

        etname.setText(owner.getName());
        etEmail.setText(owner.getEmail());


        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (etname.getText().toString() == ""){
                    t= Toast.makeText(getApplicationContext(),"Please enter a name",Toast.LENGTH_SHORT);
                    t.show();
                }else if (etEmail.getText().toString() == ""){
                    t= Toast.makeText(getApplicationContext(), "Please Enter a email", Toast.LENGTH_SHORT);
                    t.show();
                }else{
                    owner.setName(etname.getText().toString());
                    owner.setEmail(etEmail.getText().toString());
                    dh.editUser(owner);
                }

                Intent i = new Intent(getApplicationContext(),Profile.class);
                i.putExtra("owner",owner);
                startActivity(i);
            }
        });

        retur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),Profile.class);
                i.putExtra("owner",owner);
                startActivity(i);
            }
        });

        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),changePass.class);
                i.putExtra("owner",owner);
                startActivity(i);
            }
        });

    }

}
