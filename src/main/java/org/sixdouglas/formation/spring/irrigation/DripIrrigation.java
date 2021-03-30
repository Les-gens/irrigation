package org.sixdouglas.formation.spring.irrigation;

import org.sixdouglas.formation.spring.irrigation.producer.GreenHouseProducer;

import java.time.Duration;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.UUID;

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

    public Flux<Drop> followDropper(int greenHouseId, int rowId, int dropperId) {
        //TODO use the GreenHouseProducer.getDrops() function as producer, but filter the output to fit the given criteria
       return GreenHouseProducer.getDrops()
                .filter(drop -> drop.getGreenHouseId() == greenHouseId && drop.getRowId() == rowId && drop.getDropperId() == dropperId);


    }

    public Flux<DetailedDrop> followDetailedDropper(int greenHouseId, int rowId, int dropperId) {
        //TODO use the GreenHouseProducer.getDrops() function as producer, but filter the output to fit the given criteria
        //TODO    then map it to a DetailedDrop using the getDetailedDrop() function
        return GreenHouseProducer.getDrops()
                .filter(drop -> drop.getGreenHouseId() == greenHouseId && drop.getRowId() == rowId && drop.getDropperId() == dropperId)
                .flatMap(this::getDetailedDrop);

    }

    private Mono<DetailedDrop> getDetailedDrop(Drop drop) {
        return GreenHouseProducer.getDropper(
                drop.getGreenHouseId(), drop.getRowId(), drop.getDropperId())
                .map(data -> DetailedDrop.builder()
                .uuid(UUID.randomUUID().toString())
                .greenHouse(data)
                .instant(Instant.now())
                .build());
    }

    public Flux<DetailedDrop> followDetailedDropperWithError(int greenHouseId, int rowId, int dropperId) {
        //TODO use the GreenHouseProducer.getDropper() function to find the Dropper information wrap in a Greenhouse
        //TODO    then map it to build a DetailedDrop
        //TODO any error should be returned from this function

        return GreenHouseProducer.getDrops()
                .filter(drop -> drop.getGreenHouseId() == greenHouseId && drop.getRowId() == rowId && drop.getDropperId() == dropperId)
                .flatMap(this::getDetailedDrop)
                .doOnError(e -> {throw new IllegalStateException();});

    }
}
