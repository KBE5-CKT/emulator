package org.ckt.emulator.simulation.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ckt.emulator.simulation.domain.GpsData;
import org.ckt.emulator.simulation.domain.GpsPoint;
import org.ckt.emulator.simulation.infra.route.RouteApiClient;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GpsSimulationService {
    private static final int BATCH_SIZE = 60;
    private static final long INTERVAL_SECONDS = 60L;

    private final SimpMessagingTemplate messagingTemplate;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final RouteApiClient routeApiClient;

    private ScheduledFuture<?> task;

    public void startSimulation(double startLat, double startLon, double endLat, double endLon) {
        if (task != null && !task.isCancelled()) {
            log.warn("Simulation is already running.");
            return;
        }

        List<GpsPoint> route = routeApiClient.fetch(startLat, startLon, endLat, endLon);
        if (route.isEmpty()) {
            log.warn("No GPS route points available.");
            return;
        }

        final int totalPoints = route.size();
        final AtomicInteger currentIndex = new AtomicInteger(0);

        task = scheduler.scheduleAtFixedRate(() -> {
            int start = currentIndex.get();
            if (start >= totalPoints) {
                log.info("GPS simulation complete.");
                stopSimulation();
                return;
            }

            int end = Math.min(start + BATCH_SIZE, totalPoints);
            List<GpsData> batch = route.subList(start, end).stream()
                    .map(p -> new GpsData(p.lat(), p.lon(), LocalDateTime.now()))
                    .collect(Collectors.toList());

            messagingTemplate.convertAndSend("/topic/gps", batch);
            log.info("Sent GPS batch: {} ~ {}", start, end - 1);

            currentIndex.addAndGet(BATCH_SIZE);
        }, 0, INTERVAL_SECONDS, TimeUnit.SECONDS);
    }

    public void stopSimulation() {
        if (task != null && !task.isCancelled()) {
            task.cancel(false);
            log.info("GPS simulation stopped.");
        }
    }
}
