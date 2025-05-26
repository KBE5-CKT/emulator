package org.ckt.emulator.simulation.infra.route;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ckt.emulator.simulation.domain.GpsPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class RouteApiClient {
    @Value("${naver.api.client-id}")
    private final String clientId;

    @Value("${naver.api.client-secret}")
    private final String clientSecret;

    private final RestTemplate restTemplate;

    private static final String CLIENT_ID = "x-ncp-apigw-api-key-id";
    private static final String CLIENT_SECRET = "x-ncp-apigw-api-key";
    private static final String NAVER_ROUTE_URL =
            "https://maps.apigw.ntruss.com/map-direction/v1/driving?start=%s,%s&goal=%s,%s";

    public RouteApiClient(@Value("${naver.api.client-id}") String clientId,
                          @Value("${naver.api.client-secret}") String clientSecret,
                          RestTemplate restTemplate) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.restTemplate = restTemplate;
    }

    public List<GpsPoint> fetch(double startLat, double startLng, double endLat, double endLng) {
        String url = buildUrl(startLat, startLng, endLat, endLng);
        HttpEntity<?> entity = new HttpEntity<>(buildHeaders());

        ResponseEntity<DirectionResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                DirectionResponse.class
        );

        return extractGpsPoints(response.getBody());
    }

    private String buildUrl(double startLat, double startLng, double endLat, double endLng) {
        return String.format(NAVER_ROUTE_URL, startLng, startLat, endLng, endLat);
    }

    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(CLIENT_ID, clientId);
        headers.set(CLIENT_SECRET, clientSecret);
        log.info("clientId : {}", clientId);
        log.info("clientSecret : {}", clientSecret);
        return headers;
    }

    private List<GpsPoint> extractGpsPoints(DirectionResponse response) {
        if (response == null || response.getRoute() == null) {
            throw new IllegalStateException("Invalid response from Direction API");
        }

        log.info("response route: {}", response.getRoute());

        return response.getRoute().get("traoptimal").stream()
                .flatMap(route -> route.getPath().stream())
                .map(point -> new GpsPoint(point.get(1), point.get(0))) // 위도, 경도 순서 주의
                .collect(Collectors.toList());
    }
}
