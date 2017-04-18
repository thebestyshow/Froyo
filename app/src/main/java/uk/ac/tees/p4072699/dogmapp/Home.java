package uk.ac.tees.p4072699.dogmapp;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Home extends AppCompatActivity {
    Owner owner;
    DatabaseHandler dh = new DatabaseHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final Context con = this;
        final Button add = (Button) findViewById(R.id.button_doglist);
        final Button review = (Button) findViewById(R.id.button_rev);
        final Button help = (Button) findViewById(R.id.home_btn_help);
        final Button start = (Button) findViewById(R.id.button_startwalk);
        final Button rev = (Button) findViewById(R.id.button_reviews);
        final Button prof = (Button) findViewById(R.id.button_profile);
        final TextView name = (TextView) findViewById(R.id.Name_test);

        owner = (Owner) getIntent().getSerializableExtra("owner");
        name.setText(owner.getName());

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(con, Help.class);
                i.putExtra("owner", dh.getOwnerHelper(owner));
                startActivity(i);
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(con, StartWalk.class);
                i.putExtra("owner", dh.getOwnerHelper(owner));
                startActivity(i);
            }
        });

        rev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(con, ReviewList.class);
                i.putExtra("owner", dh.getOwnerHelper(owner));
                startActivity(i);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(con, DogList.class);
                i.putExtra("owner", dh.getOwnerHelper(owner));
                startActivity(i);
                setContentView(R.layout.activity_dog_list);
            }
        });

        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(con, Review.class);
                i.putExtra("owner", dh.getOwnerHelper(owner));
                startActivity(i);
                setContentView(R.layout.activity_review);
            }
        });

        prof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(con, Profile.class);
                i.putExtra("owner", dh.getOwnerHelper(owner));
                startActivity(i);
                setContentView(R.layout.activity_profile);
            }
        });
    }
}
