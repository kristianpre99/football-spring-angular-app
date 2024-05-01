package it.kristianp.footballbackendwebapp.batch.job.competition;

import it.kristianp.footballbackendwebapp.batch.job.competition.domain.CompetitionResponse;
import it.kristianp.footballbackendwebapp.batch.job.util.BatchUtils;
import it.kristianp.footballbackendwebapp.model.Competition;
import it.kristianp.footballbackendwebapp.properties.FootballAppConfigProperties;
import it.kristianp.footballbackendwebapp.repository.CompetitionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class ImportCompetitionTasklet implements Tasklet {

    private static final String COMPETITIONS_SEARCH_UEFA_PAGE_NUMBER = "/competitions/search/UEFA?page_number=";
    private final CompetitionRepository competitionRepository;
    private final RestTemplate restTemplate;
    private final FootballAppConfigProperties footballAppConfigProperties;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        try {
            int maxItemCount = footballAppConfigProperties.isBatchReaderQueryLimit() ? 2 : 6;
            for (int i = 1; i < maxItemCount; i++) {
                List<Competition> competitions = getCompetitions(i);
                if (competitions == null || competitions.isEmpty()) {
                    log.info("No more competitions, break");
                    break;
                }
                competitionRepository.saveAll(competitions);
            }
            return RepeatStatus.FINISHED;
        } catch (RuntimeException e) {
            log.error("Error API: {} ", e.getMessage());
            return null;
        }
    }

    private List<Competition> getCompetitions(int pageNum) {
        CompetitionResponse response
                = BatchUtils.getItem(footballAppConfigProperties.getTransfermarktBaseRestApiUrl() + COMPETITIONS_SEARCH_UEFA_PAGE_NUMBER + pageNum, CompetitionResponse.class, restTemplate);
        return response != null ? response.getResults() : null;
    }
}
