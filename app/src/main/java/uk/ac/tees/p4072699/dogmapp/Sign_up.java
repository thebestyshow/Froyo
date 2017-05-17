package uk.ac.tees.p4072699.dogmapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.widget.EditText;
import android.widget.Toast;

import java.util.*;

public class Sign_up extends AppCompatActivity implements View.OnClickListener {
    private DatabaseHandler dh = new DatabaseHandler(this);
    private Button sign, log;
    private EditText n, e, p1, p2;
    private String name, email, pass, conPass;
    private SQLiteDatabase db;
    private Cursor cursor;

    /*Initialises all buttons on this activity and sets onClickListeners to them */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Sign Up");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sign = (Button) findViewById(R.id.Sign_Up_btn_sign);
        log = (Button) findViewById(R.id.Log_in_btn_sign);

        sign.setOnClickListener(this);
        log.setOnClickListener(this);
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

    /* Initialises EditTexts on this activity
     * If the sign up button is pressed, details entered into the EditTexts is checked.
     * If the email EditText was empty, an appropriate toast message is created and displayed
     * If the cursor Query returns a match then the email already exists and an appropriate toast message is created and displayed
     * If any of the fields are empty, an appropriate toast message is created and displayed
     * If the two passwords entered do not match, an appropriate toast message is created and displayed
     * If the cursor query returns 0 matches and all fields are filled then the Owner is created. An appropriate toast
     * message is created and displayed, the user is then taken to the login activity where they can now login
     * If the login button is pressed then the user is taken to the login activity*/
    @Override
    public void onClick(View v) {
        Intent i;
        n = (EditText) findViewById(R.id.name_sign);
        e = (EditText) findViewById(R.id.email_sign);
        p1 = (EditText) findViewById(R.id.Pass_sign);
        p2 = (EditText) findViewById(R.id.Pass_con_sign);

        switch (v.getId()) {
            case R.id.Sign_Up_btn_sign:
                name = n.getText().toString();
                email = e.getText().toString();
                pass = p1.getText().toString();
                conPass = p2.getText().toString();
                db = dh.getReadableDatabase();
                cursor = db.rawQuery("SELECT * FROM " + dh.getOwnerLogintable() + " WHERE " + DatabaseHandler.getColEmail() + "=?", new String[]{email});
                if (email.contains("@")) {
                    if (cursor != null) {
                        if (email.equals("")) {
                            Toast.makeText(getApplicationContext(), "Please Enter an email", Toast.LENGTH_SHORT).show();
                            break;
                        } else if (cursor.getCount() > 0) {
                            Toast.makeText(getApplicationContext(), "Email Already exists", Toast.LENGTH_SHORT).show();
                            i = new Intent(getApplicationContext(), Login.class);
                            startActivity(i);
                            setContentView(R.layout.activity_login);
                            this.finish();
                            break;
                        } else if (email.equals("") || conPass.equals("")) {
                            Toast.makeText(getApplicationContext(), "Please complete all Fields", Toast.LENGTH_SHORT).show();
                            break;
                        } else if (!pass.equals(conPass)) {
                            Toast.makeText(getApplicationContext(), "Please enter matching passswords", Toast.LENGTH_SHORT).show();
                            break;
                        } else if (cursor.getCount() < 1) {
                            dh.add(new Owner(dh.getProfilesCount() + 1, name, email, conPass, new Date()));
                            i = new Intent(getApplicationContext(), Login.class);
                            Toast.makeText(getApplicationContext(), "Owner Added", Toast.LENGTH_SHORT).show();
                            startActivity(i);
                            setContentView(R.layout.activity_login);
                            this.finish();
                            break;
                        } else {
                            Toast.makeText(getApplicationContext(), "NO MATCHING IF", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Please Enter a valid email Address",Toast.LENGTH_SHORT).show();
                    break;
                }
            case R.id.Log_in_btn_sign:
                i = new Intent(getApplicationContext(), Login.class);
                startActivity(i);
                setContentView(R.layout.activity_login);
                this.finish();
                break;
        }
    }
}
