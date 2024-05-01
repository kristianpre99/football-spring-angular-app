package it.kristianp.footballbackendwebapp.batch.job.competition;

import it.kristianp.footballbackendwebapp.properties.FootballAppConfigProperties;
import it.kristianp.footballbackendwebapp.repository.CompetitionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@Configuration(ImportCompetitionsJob.NAME + ".config")
public class ImportCompetitionsJob {

    public static final String NAME = "importCompetitionsJob";
    private static final String JOB_PREFIX = NAME + ".";
    private static final String GET_AND_SAVE_COMPETITIONS = "getAndSaveCompetitions";
    private static final String GET_AND_SAVE_COMPETITIONS_TASK = JOB_PREFIX + GET_AND_SAVE_COMPETITIONS + ".task";
    private static final String GET_AND_SAVE_COMPETITIONS_STEP = JOB_PREFIX + GET_AND_SAVE_COMPETITIONS + ".step";

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    @Bean(name = NAME)
    public Job job(@Qualifier(GET_AND_SAVE_COMPETITIONS_STEP) Step getAndPersist) {
        return new JobBuilder(NAME, jobRepository)
                .start(getAndPersist)
                .build();
    }

    @Bean(name = GET_AND_SAVE_COMPETITIONS_STEP)
    public Step getAndPersist(@Qualifier(GET_AND_SAVE_COMPETITIONS_TASK) Tasklet getAndSaveCompetitionsTask) {
        return new StepBuilder(GET_AND_SAVE_COMPETITIONS_STEP, jobRepository)
                .tasklet(getAndSaveCompetitionsTask, platformTransactionManager)
                .build();
    }

    @StepScope
    @Bean(name = GET_AND_SAVE_COMPETITIONS_TASK)
    public Tasklet getAndSaveCompetitionsTask(CompetitionRepository competitionRepository, RestTemplate restTemplate, FootballAppConfigProperties footballAppConfigProperties) {
        return new ImportCompetitionTasklet(competitionRepository, restTemplate, footballAppConfigProperties);
    }
}
