package it.kristianp.footballbackendwebapp.batch.job.competition;

import it.kristianp.footballbackendwebapp.batch.job.competition.domain.CompetitionResponse;
import it.kristianp.footballbackendwebapp.model.Competition;
import it.kristianp.footballbackendwebapp.repository.CompetitionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
public class ImportCompetitionTasklet implements Tasklet {

    private static final String COMPETITIONS_SEARCH_UEFA_PAGE_NUMBER = "/competitions/search/UEFA?page_number=";
    private final CompetitionRepository competitionRepository;
    private final String basePropertyRestApiUrl;
    private final RestTemplate restTemplate;
//    private static final RestTemplate REST_TEMPLATE = new RestTemplate();


    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        try {
            for (int i = 1; i < 6; i++) {
                List<Competition> competitions = getCompetitions(i);
                if (competitions == null || competitions.isEmpty()) {
                    break;
                }
                competitionRepository.saveAll(competitions);
            }
            return RepeatStatus.FINISHED;
        } catch (NoSuchElementException e) {
            log.error("Empty leagues cities {}", e.getMessage());
            return null;
        } catch (Exception e) {
            log.error("Error API {} ", e.getMessage());
            return null;
        }
    }

    private List<Competition> getCompetitions(int pageNum) {
        ResponseEntity<CompetitionResponse> response
                = restTemplate.getForEntity(basePropertyRestApiUrl + COMPETITIONS_SEARCH_UEFA_PAGE_NUMBER + pageNum, CompetitionResponse.class);
        if (response.getBody() == null) {
            log.info("CompetitionResponse body is null, skip");
            return null;
        }
        return response.getBody().getResults();
    }
}
