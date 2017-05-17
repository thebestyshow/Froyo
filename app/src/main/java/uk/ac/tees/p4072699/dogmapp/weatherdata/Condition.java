package uk.ac.tees.p4072699.dogmapp.weatherdata;

import org.json.JSONObject;


public class Condition implements JSONPopulator {

    /*code is used to get the correct weather image icon*/
    private int code;
    private int temperature;
    private String description;

    public String getDescription() {

        return description;
    }

    public int getTemperature() {

        return temperature;
    }

    public int getCode() {

        return code;
    }

    @Override
    public void populate(JSONObject data) {
        code = data.optInt("code");
        temperature = data.optInt("temp");
        description = data.optString("text");

    }
}
