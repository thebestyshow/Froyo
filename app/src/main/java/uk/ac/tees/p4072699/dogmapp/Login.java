package uk.ac.tees.p4072699.dogmapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class Login extends AppCompatActivity implements View.OnClickListener {
    DatabaseHandler dh = new DatabaseHandler(this);
    Button sign, log, datashow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Login");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sign = (Button) findViewById(R.id.Sign_up_btn_log);
        log = (Button) findViewById(R.id.Log_in_btn_log);
        datashow = (Button) findViewById(R.id.DATABASE_SHOW);

        sign.setOnClickListener(this);
        log.setOnClickListener(this);
        datashow.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent i;
        Toast t;

        final EditText emailtxt = (EditText) findViewById(R.id.Email_log);
        final EditText passtxt = (EditText) findViewById(R.id.Pass_log);

        switch (view.getId()) {
            case R.id.DATABASE_SHOW:
                List<Owner> list = dh.getallOwners();

                Log.d("Database", "Reading all owners");

                for (Owner o : list) {
                    String log = "ID: " + o.getId() + " Name: " + o.getName() + " Email: " + o.getEmail() + " Pass: " + o.getPassword();
                    Log.d("Database ", log);
                }
                break;

            case R.id.Sign_up_btn_log:
                i = new Intent(getApplicationContext(), Sign_up.class);
                startActivity(i);
                setContentView(R.layout.activity_sign_up);
                break;

            case R.id.Log_in_btn_log:
                String email = emailtxt.getText().toString();
                String pass = passtxt.getText().toString().trim();
                SQLiteDatabase db = dh.getReadableDatabase();

                Cursor cemail = db.rawQuery("SELECT * FROM " + dh.getOwnerLogintable() + " WHERE " + dh.getColEmail() + "=?", new String[]{email});
                Cursor cursor = db.rawQuery("SELECT * FROM " + dh.getOwnerLogintable() + " WHERE " + dh.getColEmail() + "=? AND " + dh.getCOL_PASS() + "=?", new String[]{email, pass});

                if (cursor != null) {
                    if (email.equals("")) {
                        t = Toast.makeText(getApplicationContext(), "Please enter an email", Toast.LENGTH_SHORT);
                        t.show();
                        break;
                    } else if (pass.equals("")) {
                        t = Toast.makeText(getApplicationContext(), "Please enter your password", Toast.LENGTH_SHORT);
                        t.show();
                        break;
                    } else if (cemail.getCount() < 1) {
                        emailtxt.setText("");
                        passtxt.setText("");
                        Toast.makeText(getApplicationContext(), "Email not Registered.\nPlease Sign Up to continue", Toast.LENGTH_SHORT).show();
                        break;
                    } else if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                        i = new Intent(getApplicationContext(), Home.class);
                        i.putExtra("owner", dh.getOneOwner(cemail));
                        startActivity(i);
                        setContentView(R.layout.activity_home);
                        this.finish();
                        break;
                    } else {
                        Toast.makeText(getApplicationContext(), "Username or password is wrong", Toast.LENGTH_SHORT).show();
                        emailtxt.setText("");
                        passtxt.setText("");
                        break;
                    }
                }
        }
    }
}
