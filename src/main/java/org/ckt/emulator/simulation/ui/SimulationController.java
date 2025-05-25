package org.ckt.emulator.simulation.ui;

import lombok.RequiredArgsConstructor;
import org.ckt.emulator.simulation.application.GpsSimulationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/simulation")
@RequiredArgsConstructor
public class SimulationController {
    private final GpsSimulationService gpsSimulationService;

    @PostMapping("/on")
    public ResponseEntity<Void> on() {
        gpsSimulationService.startSimulation();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/off")
    public ResponseEntity<Void> off() {
        gpsSimulationService.stopSimulation();
        return ResponseEntity.ok().build();
    }
}
