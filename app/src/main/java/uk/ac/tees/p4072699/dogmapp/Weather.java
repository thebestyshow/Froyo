package uk.ac.tees.p4072699.dogmapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import uk.ac.tees.p4072699.dogmapp.weatherdata.Channel;
import uk.ac.tees.p4072699.dogmapp.weatherdata.Item;
import uk.ac.tees.p4072699.dogmapp.weatherservice.Callback;
import uk.ac.tees.p4072699.dogmapp.weatherservice.YahooWeatherService;

public class Weather extends AppCompatActivity implements Callback {
    Button home;
    Button refresh;
    Owner owner;
    DatabaseHandler dh = new DatabaseHandler(this);

    private ImageView weatherIconImageView;
    private TextView temperatureTextView;
    private TextView locationTextView;
    private TextView conditionTextView;

    private YahooWeatherService service;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Weather");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        weatherIconImageView = (ImageView)findViewById(R.id.weatherIconImageView);
        temperatureTextView = (TextView)findViewById(R.id.temperatureTextView);
        locationTextView = (TextView)findViewById(R.id.locationTextView);
        conditionTextView = (TextView)findViewById(R.id.conditionTextView);

        service = new YahooWeatherService(this);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();
        service.refreshWeather("Paris, France");

        final Context con = this;
        owner = (Owner) getIntent().getSerializableExtra("owner");
        home = (Button) findViewById(R.id.button_home);
        refresh = (Button) findViewById(R.id.button_refresh);
        refresh.performClick();

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent i = new Intent(getApplicationContext(), Home.class);
                i.putExtra("owner", dh.getOwnerHelper(owner));
                startActivity(i);
                setContentView(R.layout.activity_home);
                finish();
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
                startActivity(getIntent());
            }
        });
    }

    @Override
    public void serviceSuccess(Channel channel) {
        dialog.hide();
        Item item = channel.getItem();
        int resourceId = getResources().getIdentifier("drawable/weather_icon_" + item.getCondition().getCode(), null, getPackageName());

        @SuppressWarnings("deprecation")
        Drawable weatherIconDrawable = getResources().getDrawable(resourceId);
        weatherIconImageView.setImageDrawable(weatherIconDrawable);
        temperatureTextView.setText(item.getCondition().getTemperature()+ "\u00b0 " + channel.getUnits().getTemperature());
        conditionTextView.setText(item.getCondition().getDescription());
        locationTextView.setText(service.getLocation());
    }

    @Override
    public void serviceFailure(Exception exception) {
        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();
        dialog.dismiss();
    }
}
