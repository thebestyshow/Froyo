package uk.ac.tees.p4072699.dogmapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.DecimalFormat;

public class EditWalk extends AppCompatActivity {
    DatabaseHandler dh = new DatabaseHandler(this);
    Walk w;
    Owner owner;
    int rating;
    String comm,name;
    ImageButton p1, p2, p3, p4, p5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Edit Walk");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_walk);
        owner = (Owner) getIntent().getSerializableExtra("owner");
        w = (Walk) getIntent().getSerializableExtra("walk");
        rating = w.getRating();
        final EditText etname = (EditText) findViewById(R.id.et_name);
        final EditText etcomm = (EditText) findViewById(R.id.et_comm);
        final TextView tv_dis = (TextView) findViewById(R.id.tv_distance);
        final TextView tv_time = (TextView) findViewById(R.id.tv_time);
        final Button retur = (Button) findViewById(R.id.button_return);
        final Button save = (Button) findViewById(R.id.button_savez);
        p1 = (ImageButton) findViewById(R.id.paw_1);
        p2 = (ImageButton) findViewById(R.id.paw_2);
        p3 = (ImageButton) findViewById(R.id.paw_3);
        p4 = (ImageButton) findViewById(R.id.paw_4);
        p5 = (ImageButton) findViewById(R.id.paw_5);
        DecimalFormat df = new DecimalFormat("#.00");

        etname.setText(w.getName());
        etcomm.setText(w.getComment());
        tv_dis.setText(df.format(w.getLength()));
        tv_time.setText(String.valueOf(w.getTime()));

        if (w.getRating() == 1){
            p1.setImageResource(R.drawable.selected);
            p2.setImageResource(R.drawable.paw);
            p3.setImageResource(R.drawable.paw);
            p4.setImageResource(R.drawable.paw);
            p5.setImageResource(R.drawable.paw);
        }else if(w.getRating() ==2){
            p2.setImageResource(R.drawable.selected);
            p1.setImageResource(R.drawable.selected);
            p3.setImageResource(R.drawable.paw);
            p4.setImageResource(R.drawable.paw);
            p5.setImageResource(R.drawable.paw);
        }else if(w.getRating() ==3){
            p3.setImageResource(R.drawable.selected);
            p2.setImageResource(R.drawable.selected);
            p1.setImageResource(R.drawable.selected);
            p4.setImageResource(R.drawable.paw);
            p5.setImageResource(R.drawable.paw);
        }else if(w.getRating() ==4){
            p4.setImageResource(R.drawable.selected);
            p2.setImageResource(R.drawable.selected);
            p3.setImageResource(R.drawable.selected);
            p1.setImageResource(R.drawable.selected);
            p5.setImageResource(R.drawable.paw);
        }else if(w.getRating() ==5){
            p5.setImageResource(R.drawable.selected);
            p2.setImageResource(R.drawable.selected);
            p3.setImageResource(R.drawable.selected);
            p4.setImageResource(R.drawable.selected);
            p1.setImageResource(R.drawable.selected);
        }

        p1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                rating = 1;
                p1.setImageResource(R.drawable.selected);
                p2.setImageResource(R.drawable.paw);
                p3.setImageResource(R.drawable.paw);
                p4.setImageResource(R.drawable.paw);
                p5.setImageResource(R.drawable.paw);
            }
        });

        p2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                rating = 2;
                p2.setImageResource(R.drawable.selected);
                p1.setImageResource(R.drawable.selected);
                p3.setImageResource(R.drawable.paw);
                p4.setImageResource(R.drawable.paw);
                p5.setImageResource(R.drawable.paw);
            }
        });

        p3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                rating = 3;
                p3.setImageResource(R.drawable.selected);
                p2.setImageResource(R.drawable.selected);
                p1.setImageResource(R.drawable.selected);
                p4.setImageResource(R.drawable.paw);
                p5.setImageResource(R.drawable.paw);

            }
        });

        p4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                rating = 4;
                p4.setImageResource(R.drawable.selected);
                p2.setImageResource(R.drawable.selected);
                p3.setImageResource(R.drawable.selected);
                p1.setImageResource(R.drawable.selected);
                p5.setImageResource(R.drawable.paw);
            }
        });

        p5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                rating = 5;
                p5.setImageResource(R.drawable.selected);
                p2.setImageResource(R.drawable.selected);
                p3.setImageResource(R.drawable.selected);
                p4.setImageResource(R.drawable.selected);
                p1.setImageResource(R.drawable.selected);
            }
        });

        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                w.setComment(etcomm.getText().toString());
                w.setName(etname.getText().toString());
                w.setRating(rating);
                dh.editWalk(w);
                Intent i = new Intent(getApplicationContext(),ReviewView.class);
                i.putExtra("walk",w);
                i.putExtra("owner",owner);
                startActivity(i);
            }
        });
        retur.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                onBackPressed();
            }
        });
    }
}
