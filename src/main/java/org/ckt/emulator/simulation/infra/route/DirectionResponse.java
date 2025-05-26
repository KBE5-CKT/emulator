package org.ckt.emulator.simulation.infra.route;

import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class DirectionResponse {
    private Map<String, List<Route>> route;

    @Getter
    public static class Route {
        private List<List<Double>> path;
    }
}

