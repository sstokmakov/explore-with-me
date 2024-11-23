package ru.tokmakov.stat;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.tokmakov.dto.HitDto;
import ru.tokmakov.dto.StatsResponseDto;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class StatClient {
    private static final String URL = "http://stats-server:9090";
    private final RestTemplate restTemplate;

    public StatClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void recordHit(HitDto hitDto) {
        restTemplate.postForEntity(URL + "/hit", hitDto, Void.class);
    }

    public List<StatsResponseDto> getStats(String start, String end, List<String> uris, boolean unique) {
        StringBuilder uriBuilder = new StringBuilder();

        if (uris != null && !uris.isEmpty()) {
            uriBuilder.append(String.join("&uris=", uris));
        }

        String url = String.format(URL + "?start=%s&end=%s%s%s",
                URLEncoder.encode(start, StandardCharsets.UTF_8),
                URLEncoder.encode(end, StandardCharsets.UTF_8),
                !uriBuilder.isEmpty() ? "&" : "",
                !uriBuilder.isEmpty() ? URLEncoder.encode(uriBuilder.toString(), StandardCharsets.UTF_8) : "");

        if (unique) {
            url += "&unique=true";
        }

        ResponseEntity<List<StatsResponseDto>> response = restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                });
        return response.getBody();
    }

    public Boolean existsByIpAndUri(String ip, String uri) {
        return restTemplate.getForObject(URL + "?ip=" + ip + "&uri=" + uri, Boolean.class);
    }
}
