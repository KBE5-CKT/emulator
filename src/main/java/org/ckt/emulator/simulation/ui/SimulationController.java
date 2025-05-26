package org.ckt.emulator.simulation.ui;

import lombok.RequiredArgsConstructor;
import org.ckt.emulator.simulation.application.GpsSimulationService;
import org.ckt.emulator.simulation.ui.dto.GpsSimulationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/simulation")
@RequiredArgsConstructor
public class SimulationController {
    private final GpsSimulationService gpsSimulationService;

    @PostMapping("/on")
    public ResponseEntity<Void> startSimulation(
            @RequestBody GpsSimulationRequest gpsSimulationRequest
    ) {
        gpsSimulationService.startSimulation(
                gpsSimulationRequest.startLat(),
                gpsSimulationRequest.startLon(),
                gpsSimulationRequest.endLat(),
                gpsSimulationRequest.endLon()
        );
        return ResponseEntity.ok().build();
    }

    @PostMapping("/off")
    public ResponseEntity<Void> off() {
        gpsSimulationService.stopSimulation();
        return ResponseEntity.ok().build();
    }
}
