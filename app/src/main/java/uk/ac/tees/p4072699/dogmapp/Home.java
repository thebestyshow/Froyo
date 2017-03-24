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

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(con, DogList.class);
                startActivity(intent);
            }
        });

        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(con, Review.class);
                startActivity(intent);
            }
        });
    }
}
