package it.kristianp.footballbackendwebapp.batch.job.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@UtilityClass
public class BatchUtils {

    public <T> T getItem(String url, Class<T> responseType, RestTemplate restTemplate) {
        try {
            String normalizeUrl = normalizeUrl(url);
            log.info("Request api: {}", normalizeUrl);
            ResponseEntity<T> response = restTemplate.getForEntity(normalizeUrl, responseType);
            if (response.getBody() == null) {
                log.info(responseType.getSimpleName() + " body is null, skip");
                return null;
            }
            return response.getBody();
        } catch (RestClientException e) {
            log.error("Error calling API: {} ", e.getMessage());
            return null;
        }
    }

    private static String normalizeUrl(String url) {
        try {
            return new URI(url).normalize().toString();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
