package org.ckt.emulator.simulation.scheduler;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ckt.emulator.simulation.domain.GpsPoint;

@Slf4j
@RequiredArgsConstructor
public class SimulationTask implements Runnable {
    private final List<GpsPoint> route;
    private final AtomicInteger index = new AtomicInteger(0);

    @Override
    public void run() {
        if (index.get() >= route.size()) {
            return;
        }

        GpsPoint current = route.get(index.getAndIncrement());
        log.info("위치 전송 -> lat : {}, lon : {}", current.lat(), current.lon());
    }
}
