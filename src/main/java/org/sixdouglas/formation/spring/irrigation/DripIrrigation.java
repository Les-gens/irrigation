package org.sixdouglas.formation.spring.irrigation;

import java.time.Duration;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class DripIrrigation {
    private static Logger LOGGER = LoggerFactory.getLogger(DripIrrigation.class);

    public Flux<Drop> followDrops() {
        Flux<Drop> flux = Flux
                            .interval(Duration.ofMillis(20))
                            .map(receivedLong -> Drop
                                            .builder()
                                            .greenHouseId(1)
                                            .rowId(1)
                                            .dropperId(1)
                                            .instant(Instant.now())
                                            .build());
        return flux;
    }
}
