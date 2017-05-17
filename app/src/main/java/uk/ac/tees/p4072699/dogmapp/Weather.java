package uk.ac.tees.p4072699.dogmapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
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
    private Button home;
    private Button refresh;
    private Owner owner;
    private DatabaseHandler dh = new DatabaseHandler(this);

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        weatherIconImageView = (ImageView) findViewById(R.id.weatherIconImageView);
        temperatureTextView = (TextView) findViewById(R.id.temperatureTextView);
        locationTextView = (TextView) findViewById(R.id.locationTextView);
        conditionTextView = (TextView) findViewById(R.id.conditionTextView);

        service = new YahooWeatherService(this);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();
        /*the location hard coded here should be gotten from user.*/
        service.refreshWeather("Middlesbrough");

        final Context con = this;
        owner = (Owner) getIntent().getSerializableExtra("owner");
        refresh = (Button) findViewById(R.id.button_refresh);
        refresh.performClick();

        /*reopens activity with updated info*/
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
                startActivity(getIntent());
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent i = new Intent(getApplicationContext(), Home.class);
            i.putExtra("owner", owner);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void serviceSuccess(Channel channel) {
        dialog.hide();
        Item item = channel.getItem();
        /*uses item.code and attaches to weather_icon string to find correct image in drawables.*/
        int resourceId = getResources().getIdentifier("drawable/weather_icon_" + item.getCondition().getCode(), null, getPackageName());

        @SuppressWarnings("deprecation")
        Drawable weatherIconDrawable = getResources().getDrawable(resourceId);
        weatherIconImageView.setImageDrawable(weatherIconDrawable);
        temperatureTextView.setText(item.getCondition().getTemperature() + "\u00b0 " + channel.getUnits().getTemperature());
        conditionTextView.setText(item.getCondition().getDescription());
        locationTextView.setText(service.getLocation());
    }

    @Override
    public void serviceFailure(Exception exception) {
        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();
        dialog.dismiss();
    }
}
