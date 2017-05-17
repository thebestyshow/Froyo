package uk.ac.tees.p4072699.dogmapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class changePass extends AppCompatActivity {
    private DatabaseHandler dh = new DatabaseHandler(this);
    private Owner owner;
    private Toast t;


    /* Initialises all EditTexts and buttons. Once Save is pressed, the passwords that have been entered are checked,
    * if the entered passwords pass the validation then the new password is saved to the database in place of the old.
    * If the entered passwords do not pass the validation, then a toast message is displayed letting the user know that
    * the passwords they entered were incorrect*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Change Password");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        owner = (Owner) getIntent().getSerializableExtra("owner");
        final EditText oldPass = (EditText) findViewById(R.id.et_old_pass);
        final EditText newPass = (EditText) findViewById(R.id.et_new_pass);
        final EditText conNewPass = (EditText) findViewById(R.id.et_con_new_pass);
        final Button save = (Button) findViewById(R.id.button_savez);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Profile.class);
                i.putExtra("owner", owner);
                if (!oldPass.getText().toString().equals(owner.getPassword())) {
                    t = Toast.makeText(getApplicationContext(), "Please Enter your current password Correctly", Toast.LENGTH_SHORT);
                    t.show();
                    oldPass.setText("");
                    newPass.setText("");
                    conNewPass.setText("");
                } else {
                    if (!newPass.getText().toString().equals(conNewPass.getText().toString())) {
                        t = Toast.makeText(getApplicationContext(), "Please ensure new passwords match", Toast.LENGTH_SHORT);
                        t.show();
                        newPass.setText("");
                        conNewPass.setText("");
                    } else {
                        owner.setPassword(conNewPass.getText().toString());
                        dh.editUser(owner);
                        startActivity(i);
                    }
                }
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
