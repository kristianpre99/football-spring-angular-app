package it.kristianp.footballbackendwebapp.batch.job.competition;

import it.kristianp.footballbackendwebapp.properties.BatchProperties;
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
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${base.transfermarkt.rest.api.url}")
    private String basePropertyRestApiUrl;


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
    public Tasklet getAndSaveCompetitionsTask(CompetitionRepository competitionRepository, RestTemplate restTemplate, BatchProperties batchProperties) {
        return new ImportCompetitionTasklet(competitionRepository, basePropertyRestApiUrl, restTemplate, batchProperties);
    }

//    private static final String GET_AND_PERSIST = "getAndPersist";
//    private static final String GET_AND_PERSIST_STEP = JOB_PREFIX + GET_AND_PERSIST + ".step";
//    private static final String GET = JOB_PREFIX + "get";
//    private static final String WRITER = JOB_PREFIX + "writer";
//    private static final String TASK_EXECUTOR = JOB_PREFIX + "taskExecutor";

//    @Value("${base.transfermarkt.rest.api.url}")
//    private String basePropertyRestApiUrl;
//
//    @Value("${jobs.importCompetitionsJob.chunk.size:1}")
//    private Integer chunkSize;

//    @Bean(name = GET_AND_PERSIST_STEP)
//    public Step getAndPersist(@Qualifier(GET) ItemStreamReader<CompetitionResponse> reader,
//                              @Qualifier(WRITER) ItemWriter<CompetitionResponse> writer,
//                              @Qualifier(TASK_EXECUTOR) TaskExecutor taskExecutor) {
//        return new StepBuilder(GET_AND_PERSIST_STEP, jobRepository)
//                .<CompetitionResponse, CompetitionResponse>chunk(chunkSize, platformTransactionManager)
//                .reader(reader)
//                .writer(writer)
//                .taskExecutor(taskExecutor)
//                .build();
//    }

//    @Bean(name = GET)
//    public ItemStreamReader<CompetitionResponse> itemReader() {
//        return new ImportCompetitionReader(REST_TEMPLATE, basePropertyRestApiUrl);
//    }
//
//    @Bean(name = WRITER)
//    public ItemWriter<CompetitionResponse> writer(CompetitionRepository competitionRepository) throws Exception {
//        return items -> items.forEach(f -> {
//            competitionRepository.saveAll(f.getResults());
//        });
//    }

//    @Bean(name = WRITER)
//    public RepositoryItemWriter<Competition> writer() {
//        RepositoryItemWriter<Competition> writer = new RepositoryItemWriter<>();
//        writer.setRepository(competitionRepository);
//        writer.setMethodName("save");
//        return writer;
//    }

//
//    @Bean(name = TASK_EXECUTOR)
//    public TaskExecutor taskExecutor() {
//        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
//        asyncTaskExecutor.setConcurrencyLimit(10);
//        return asyncTaskExecutor;
//    }
}
