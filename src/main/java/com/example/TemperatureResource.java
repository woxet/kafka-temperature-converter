package com.example;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/temperature")
public class TemperatureResource {

    @Inject
    TemperatureConsumer temperatureConsumer;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getLatestTemperature() {
        return "Latest Temperature (Fahrenheit): " + temperatureConsumer.getLatestTemperature();
    }
}
