package org.ckt.emulator.simulation.domain;

import java.time.LocalDateTime;

public record GpsData(double latitude, double longitude, LocalDateTime timestamp) {}

