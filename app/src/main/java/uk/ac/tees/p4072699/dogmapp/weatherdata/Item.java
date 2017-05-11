package uk.ac.tees.p4072699.dogmapp.weatherdata;

import org.json.JSONObject;

/**
 * Created by besty on 09/05/2017.
 */

public class Item implements JSONPopulator{

    private Condition condition;

    public Condition getCondition() {
        return condition;
    }

    @Override
    public void populate(JSONObject data) {
        condition = new Condition();
        condition.populate(data.optJSONObject("condition"));

    }
}
