package it.kristianp.footballbackendwebapp.batch.job.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.NoSuchElementException;

@Slf4j
@UtilityClass
public class BatchUtils {

    public  <T> T getItem(String url, Class<T> responseType, RestTemplate restTemplate) {
        try {
            ResponseEntity<T> response = restTemplate.getForEntity(url, responseType);
            if (response.getBody() == null) {
                log.info(responseType.getSimpleName() + " body is null, skip");
                return null;
            }
            return response.getBody();
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
            return null;
        } catch (Exception e) {
            log.error("Error API {} ", e.getMessage());
            return null;
        }
    }
}
