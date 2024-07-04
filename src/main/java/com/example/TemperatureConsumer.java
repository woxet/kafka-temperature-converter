package com.example;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.LinkedList;
import java.util.List;

@ApplicationScoped
@Path("/temperature")
public class TemperatureConsumer {

    private List<Integer> temperatures = new LinkedList<>();

    @Incoming("fahrenheit")
    public void consume(int tempFahrenheit) {
        if (temperatures.size() >= 10) {
            temperatures.remove(0);
        }
        temperatures.add(tempFahrenheit);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Integer> getTemperatures() {
        return temperatures;
    }
}
