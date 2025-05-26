package org.ckt.emulator.simulation.domain;

import java.time.LocalDateTime;

public record GpsData(double lat, double lon, LocalDateTime timestamp) {}

