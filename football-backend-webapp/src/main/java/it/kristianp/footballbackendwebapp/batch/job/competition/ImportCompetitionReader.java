package it.kristianp.footballbackendwebapp.batch.job.competition;

import it.kristianp.footballbackendwebapp.batch.job.competition.domain.CompetitionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.NoSuchElementException;

@Slf4j
@Deprecated
public class ImportCompetitionReader implements ItemStreamReader<CompetitionResponse> {

    private final RestTemplate restTemplate;
    private final String competitionsUrl;

    public ImportCompetitionReader(RestTemplate restTemplate, String basePropertyRestApiUrl) {
        this.restTemplate = restTemplate;
        this.competitionsUrl = basePropertyRestApiUrl + "/competitions/search/UEFA?page_number=1";
    }

    @Override
    public CompetitionResponse read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        try {
            ResponseEntity<CompetitionResponse> response
                    = restTemplate.getForEntity(competitionsUrl, CompetitionResponse.class);
            if (response.getBody() == null) {
                log.info("CompetitionResponse body is null, skip");
                return null;
            }
            return response.getBody();
        } catch (NoSuchElementException e) {
            log.error("Empty list cities {}", e.getMessage());
            return null;
        } catch (Exception e) {
            log.error("Error API {} ", e.getMessage());
            return null;
        }
    }
}
