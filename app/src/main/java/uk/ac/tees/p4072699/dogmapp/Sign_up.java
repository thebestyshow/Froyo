package uk.ac.tees.p4072699.dogmapp;

import android.content.Intent;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import android.widget.EditText;
import android.widget.Toast;
import java.util.*;

public class Sign_up extends AppCompatActivity implements View.OnClickListener {

    DatabaseHandler dh = new DatabaseHandler(this);
    Button sign,log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        sign = (Button) findViewById(R.id.Sign_Up_btn);
        log = (Button) findViewById(R.id.login_btn);
        sign.setOnClickListener(this);
        log.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i;
        Toast t;

        switch(v.getId()){
            case R.id.Sign_Up_btn:
                final EditText e = (EditText) findViewById(R.id.email_txt);
                final EditText p1 = (EditText) findViewById(R.id.Pass_txt1);
                final EditText p2 = (EditText) findViewById(R.id.Pass_txt2);


                if (e.getText().toString().equals("")){
                    t = Toast.makeText(getApplicationContext(), "Please Enter an email", Toast.LENGTH_SHORT);
                    t.show();
                    break;
                }
                else if (!p1.getText().toString().equals(p2.getText().toString())) {
                    t = Toast.makeText(getApplicationContext(),"Please enter matching passswords", Toast.LENGTH_SHORT);
                    t.show();
                    break;
                }else{
                    dh.add(new Owner(dh.getProfilesCount()+1,"TEST" + dh.getProfilesCount()+1, e.toString(),p2.toString(),new Date()));
                    i = new Intent(getApplicationContext(),HomeActivity.class);
                    t = Toast.makeText(getApplicationContext(),"Owner Added", Toast.LENGTH_SHORT);
                    t.show();
                    startActivity(i);
                    setContentView(R.layout.activity_home);
                    break;
                }
        }
    }
}
