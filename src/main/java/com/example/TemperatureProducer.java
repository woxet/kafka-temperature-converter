import io.quarkus.runtime.StartupEvent;
import io.smallrye.reactive.messaging.annotations.Emitter;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import javax.enterprise.context.ApplicationScoped;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class TemperatureProducer {

    @Inject
    @Channel("generated-temperature")
    Emitter<Integer> temperatureEmitter;

    private Random random = new Random();

    void onStart(@Observes StartupEvent ev) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::generateTemperature, 0, 5, TimeUnit.SECONDS);
    }

    @Outgoing("generated-temperature")
    public Integer generateTemperature() {
        return random.nextInt(40); // Génère une température entre 0 et 40 degrés Celsius
    }
}
