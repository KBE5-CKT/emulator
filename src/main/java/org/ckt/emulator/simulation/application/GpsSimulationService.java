package org.ckt.emulator.simulation.application;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.ckt.emulator.simulation.domain.GpsData;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GpsSimulationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> task;

    public void startSimulation() {
        if (task != null && !task.isCancelled()) return;

        task = scheduler.scheduleAtFixedRate(() -> {
            GpsData gps = generateRandomGps();
            messagingTemplate.convertAndSend("/topic/gps", gps);
        }, 0, 60, TimeUnit.SECONDS);
    }

    public void stopSimulation() {
        if (task != null) task.cancel(true);
    }

    private GpsData generateRandomGps() {
        double lat = 37.5 + Math.random() * 0.1;
        double lon = 126.9 + Math.random() * 0.1;
        return new GpsData(lat, lon, LocalDateTime.now());
    }
}
