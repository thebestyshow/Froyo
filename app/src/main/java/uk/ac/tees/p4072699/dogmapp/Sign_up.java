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

    DatabaseHandler dh = new DatabaseHandler(this);
    Button sign, log;

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

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onClick(View v) {
        Intent i;
        final EditText n = (EditText) findViewById(R.id.name_sign);
        final EditText e = (EditText) findViewById(R.id.email_sign);
        final EditText p1 = (EditText) findViewById(R.id.Pass_sign);
        final EditText p2 = (EditText) findViewById(R.id.Pass_con_sign);

        switch (v.getId()) {
            case R.id.Sign_Up_btn_sign:
                String name = n.getText().toString();
                String email = e.getText().toString();
                String pass = p1.getText().toString();
                String conPass = p2.getText().toString();
                SQLiteDatabase db = dh.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT * FROM " + dh.getOwnerLogintable() + " WHERE " + DatabaseHandler.getColEmail() + "=?", new String[]{email});

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
            case R.id.Log_in_btn_sign:
                i = new Intent(getApplicationContext(), Login.class);
                startActivity(i);
                setContentView(R.layout.activity_login);
                this.finish();
                break;
        }
    }
}
