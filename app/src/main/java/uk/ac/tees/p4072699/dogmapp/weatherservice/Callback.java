package uk.ac.tees.p4072699.dogmapp.weatherservice;

import uk.ac.tees.p4072699.dogmapp.weatherdata.Channel;

public interface Callback {
    void serviceSuccess(Channel channel);
    void serviceFailure(Exception exception);
}
