package org.ckt.emulator.simulation.ui.dto;

public record GpsSimulationRequest (
    double startLat,
    double startLon,
    double endLat,
    double endLon
){

}
