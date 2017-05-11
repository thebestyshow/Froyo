package uk.ac.tees.p4072699.dogmapp.weatherdata;

import org.json.JSONObject;

/**
 * Created by besty on 09/05/2017.
 */

public class Channel implements JSONPopulator{
    private Item item;
    private Units units;

    public Units getUnits(){
        return units;
    }

    public Item getItem(){
        return item;
    }
        @Override
    public void populate(JSONObject data) {
        units = new Units();
        units.populate(data.optJSONObject("units"));

        item = new Item();
        item.populate(data.optJSONObject("item"));
    }
}
