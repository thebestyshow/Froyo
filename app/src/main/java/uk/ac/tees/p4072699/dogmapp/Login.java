package uk.ac.tees.p4072699.dogmapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity implements View.OnClickListener {
    private DatabaseHandler dh = new DatabaseHandler(this);
    private Button sign, log, datashow;
    private EditText emailtxt, passtxt;
    private String pass, email;
    private Cursor cemail, cursor;

    /*Initialises all TextViews and buttons */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Login");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sign = (Button) findViewById(R.id.Sign_up_btn_log);
        log = (Button) findViewById(R.id.Log_in_btn_log);

        sign.setOnClickListener(this);
        log.setOnClickListener(this);
    }

    /*Once the user has pressed the sign up button, they will be taken to the sign up activity.
    * If the user presses the login button, the details that they entered will be checked.
    * If the email field is empty then an appropriate toast message is created and displayed.
    * If the password field is empty then an appropriate toast message is created and displayed.
    * If the email does not exist in the database, then an appropriate toast message is created and displayed.
    * If the Email or password is wrong then an appropriate toast message is created and displayed
    * if the Email and password entered match with a record in the database, the the user is logged in a taken to the Home activity
    */
    @Override
    public void onClick(View view) {
        Intent i;
        Toast t;

        emailtxt = (EditText) findViewById(R.id.Email_log);
        passtxt = (EditText) findViewById(R.id.Pass_log);

        switch (view.getId()) {
            case R.id.Sign_up_btn_log:
                i = new Intent(getApplicationContext(), Sign_up.class);
                startActivity(i);
                setContentView(R.layout.activity_sign_up);
                break;

            case R.id.Log_in_btn_log:
                email = emailtxt.getText().toString();
                pass = passtxt.getText().toString().trim();
                SQLiteDatabase db = dh.getReadableDatabase();

                cemail = db.rawQuery("SELECT * FROM " + dh.getOwnerLogintable() + " WHERE " + dh.getColEmail() + "=?", new String[]{email});
                cursor = db.rawQuery("SELECT * FROM " + dh.getOwnerLogintable() + " WHERE " + dh.getColEmail() + "=? AND " + dh.getCOL_PASS() + "=?", new String[]{email, pass});

                if(email.contains("@")){
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
                }else{
                    t = Toast.makeText(getApplicationContext(),"Please enter a valid email address",Toast.LENGTH_SHORT);
                    t.show();
                    emailtxt.setText("");
                }
        }
    }
}
