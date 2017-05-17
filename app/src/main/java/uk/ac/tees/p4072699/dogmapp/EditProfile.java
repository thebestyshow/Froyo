package uk.ac.tees.p4072699.dogmapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditProfile extends AppCompatActivity {
    private DatabaseHandler dh = new DatabaseHandler(this);
    private Owner owner;
    private Toast t;
    private Button changePass, save;
    private EditText etname, etEmail;

    @Override
    /*Sets the title, enables Android Up navigation, and sets the EditText fields for
    * name, email, change password, and save.*/
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Edit Profile");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etname = (EditText) findViewById(R.id.etname);
        etEmail = (EditText) findViewById(R.id.etEmail);
        changePass = (Button) findViewById(R.id.button_change);
        save = (Button) findViewById(R.id.button_save2);
        owner = (Owner) getIntent().getSerializableExtra("owner");
        etname.setText(owner.getName());
        etEmail.setText(owner.getEmail());

        /*Action listener for save button and logic for saved changed*/
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etname.getText().toString() == "") {
                    t = Toast.makeText(getApplicationContext(), "Please enter a name", Toast.LENGTH_SHORT);
                    t.show();
                } else if (etEmail.getText().toString() == "") {
                    t = Toast.makeText(getApplicationContext(), "Please Enter a email", Toast.LENGTH_SHORT);
                    t.show();
                } else {
                    owner.setName(etname.getText().toString());
                    owner.setEmail(etEmail.getText().toString());
                    dh.editUser(owner);
                }

                Intent i = new Intent(getApplicationContext(), Profile.class);
                i.putExtra("owner", owner);
                startActivity(i);
            }
        });

        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), changePass.class);
                i.putExtra("owner", owner);
                startActivity(i);
            }
        });
    }
    /*checks if a menu item is pressed and if it is, the user is returned to the previous screen */
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
