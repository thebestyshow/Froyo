package uk.ac.tees.p4072699.dogmapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final Context con = this;
        final Button add = (Button) findViewById(R.id.button_adddog);
        final Button review = (Button) findViewById(R.id.button_rev);
        final Button help = (Button) findViewById(R.id.home_btn_help);

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(con,Help.class);
                startActivity(i);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(con, DogList.class);
                startActivity(i);
                setContentView(R.layout.activity_dog_list);
            }
        });

        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(con, Review.class);
                startActivity(i);
                setContentView(R.layout.activity_review);
            }
        });
    }
}
