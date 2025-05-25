package org.ckt.emulator.simulation.application;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ckt.emulator.simulation.domain.GpsPoint;
import org.ckt.emulator.simulation.infra.RouteApiClient;
import org.ckt.emulator.simulation.scheduler.SimulationTask;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SimulationService {
    private final RouteApiClient routeApiClient;
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private Future<?> task;

    public void on() {
        if (task != null && !task.isDone()) {
            return;
        }
        List<GpsPoint> path = routeApiClient.fetch("서울", "대전");
        task = executor.scheduleAtFixedRate(new SimulationTask(path), 0 , 60, TimeUnit.SECONDS);
        log.info("시뮬레이션 시작");
    }

    public void off() {
        if (task != null) {
            task.cancel(true);
            log.info("시뮬레이션 종료");
        }
    }
}
