package org.ckt.emulator.simulation.infra;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.ckt.emulator.simulation.domain.GpsPoint;
import org.springframework.stereotype.Component;

@Component
public class RouteApiClient {
    public List<GpsPoint> fetch(String from, String to) {
        // TODO: 외부 지도 API 호출
        return IntStream.range(0, 10)
                .mapToObj(i -> new GpsPoint(37.5665 + i * 0.01, 126.9780 + i * 0.01))
                .collect(Collectors.toList());

    }
}
