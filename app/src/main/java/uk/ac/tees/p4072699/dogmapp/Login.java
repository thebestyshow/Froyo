package uk.ac.tees.p4072699.dogmapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity implements View.OnClickListener {

    DatabaseHandler dh = new DatabaseHandler(this);
    Button sign,log;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sign = (Button) findViewById(R.id.Sign_up_btn_log);
        log = (Button) findViewById(R.id.Log_in_btn_log);

        sign.setOnClickListener(this);
        log.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent i;
        Toast t;

        final EditText emailtxt = (EditText) findViewById(R.id.Email_log);
        final EditText passtxt = (EditText) findViewById(R.id.Pass_Log);

        switch(view.getId()){
            case R.id.Sign_up_btn_log:
                i = new Intent(getApplicationContext(),Sign_up.class);
                startActivity(i);
                setContentView(R.layout.activity_sign_up);
                break;
            case R.id.Log_in_btn_log:
                String email = emailtxt.getText().toString().trim();
                String pass = passtxt.getText().toString().trim();

                String storedPass = dh.getSinlgeEntry_log(email);

                //Toast.makeText(getApplicationContext(), String.valueOf(dh.getProfilesCount()),Toast.LENGTH_SHORT).show();
                if (email.equals("")){
                    t= Toast.makeText(getApplicationContext(),"Please enter an email", Toast.LENGTH_SHORT);
                    t.show();
                    break;
                }
                else if(pass.equals("")){
                    t = Toast.makeText(getApplicationContext(),"Please enter your password",Toast.LENGTH_SHORT);
                    t.show();

                    break;
                }
                else if (storedPass.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Email not found. Sign up to continue",Toast.LENGTH_SHORT).show();
                    break;
                }
                else if (pass.equals(storedPass))
                {
                    Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT).show();
                    i = new Intent(getApplicationContext(),Home.class);
                    startActivity(i);
                    setContentView(R.layout.activity_home);
                    break;
                }else
                {
                    Toast.makeText(getApplicationContext(),"Email or password does not match",Toast.LENGTH_SHORT).show();
                    break;
                }

        }
    }
}
