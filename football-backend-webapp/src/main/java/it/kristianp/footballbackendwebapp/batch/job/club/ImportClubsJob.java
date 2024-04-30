package it.kristianp.footballbackendwebapp.batch.job.club;

import it.kristianp.footballbackendwebapp.batch.job.club.domain.ClubResponse;
import it.kristianp.footballbackendwebapp.batch.job.util.BatchUtils;
import it.kristianp.footballbackendwebapp.model.Club;
import it.kristianp.footballbackendwebapp.model.Competition;
import it.kristianp.footballbackendwebapp.model.Participation;
import it.kristianp.footballbackendwebapp.properties.BatchProperties;
import it.kristianp.footballbackendwebapp.repository.ClubRepository;
import it.kristianp.footballbackendwebapp.repository.ParticipationRepository;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
@Configuration(ImportClubsJob.NAME + ".config")
public class ImportClubsJob {

    public static final String NAME = "importClubsJob";
    private static final String QUERY_COMPETITIONS = "select c from Competition c";
    private static final String QUERY_CLUBS = "select c from Club c where c.image is null";
    private static final String JOB_PREFIX = NAME + ".";

    private static final String READ_AND_PERSIST = "readAndPersist";
    private static final String READ_AND_PERSIST_STEP = JOB_PREFIX + READ_AND_PERSIST + ".step";
    private static final String READER = JOB_PREFIX + "reader";
    private static final String PROCESSOR = JOB_PREFIX + "processor";
    private static final String WRITER = JOB_PREFIX + "writer";

    private static final String ENRICH_CLUBS = "enrichClubs";
    private static final String ENRICH_CLUBS_STEP = JOB_PREFIX + ENRICH_CLUBS + ".step";
    private static final String ENRICH_STEP_READER = ENRICH_CLUBS_STEP + ".reader";
    private static final String ENRICH_STEP_PROCESSOR = ENRICH_CLUBS_STEP + ".processor";
    private static final String ENRICH_STEP_WRITER = ENRICH_CLUBS_STEP + ".writer";

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final RestTemplate restTemplate;
    private final ParticipationRepository participationRepository;
    private final ClubRepository clubRepository;
    private final BatchProperties batchProperties;

    @Value("${base.transfermarkt.rest.api.url}")
    private String basePropertyRestApiUrl;
    @Value("${importClubsJob.chunk.size:5}")
    private Integer chunkSize;

    @Bean(name = NAME)
    public Job job(
            @Qualifier(READ_AND_PERSIST_STEP) Step readAndPersist,
            @Qualifier(ENRICH_CLUBS_STEP) Step enrichClubs
    ) {
        return new JobBuilder(NAME, jobRepository)
                .start(readAndPersist)
                .next(enrichClubs)
                .build();
    }

    @Bean(name = READ_AND_PERSIST_STEP)
    public Step readAndPersist(@Qualifier(READER) ItemReader<Competition> reader,
                               @Qualifier(PROCESSOR) ItemProcessor<Competition, ClubResponse> processor,
                               @Qualifier(WRITER) ItemWriter<ClubResponse> writer) {
        return new StepBuilder(READ_AND_PERSIST_STEP, jobRepository)
                .<Competition, ClubResponse>chunk(chunkSize, platformTransactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean(name = READER)
    protected ItemReader<Competition> competitionItemReader(EntityManagerFactory entityManagerFactory) throws Exception {
        JpaCursorItemReader<Competition> jpaCursorItemReader = new JpaCursorItemReaderBuilder<Competition>()
                .name(READER)
                .entityManagerFactory(entityManagerFactory)
                .queryString(QUERY_COMPETITIONS)
                .build();

        if (batchProperties.isLimited()) {
            jpaCursorItemReader.setMaxItemCount(3);
        }
        return jpaCursorItemReader;
    }

    @Bean(name = PROCESSOR)
    public ItemProcessor<Competition, ClubResponse> competitionClubResponseItemProcessor() {
        return item -> {
            String url = basePropertyRestApiUrl + String.format("competitions/%s/clubs", item.getId());
            ClubResponse clubResponse = BatchUtils.getItem(url, ClubResponse.class, restTemplate);
            if (clubResponse == null) {
                return null;
            }
            clubResponse.setCompetition(item);
            return clubResponse;
        };
    }

    @Bean(name = WRITER)
    public ItemWriter<ClubResponse> clubResponseItemWriter() {
        return items -> items.forEach(f -> {
            Competition competition = f.getCompetition();
            for (Club club : f.getClubs()) {
                Participation participation = new Participation();
                participation.setClub(club);
                participation.setCompetition(competition);
                participation.setSeason(getSeason());
                participationRepository.save(participation);
            }
        });
    }

    @Bean(name = ENRICH_CLUBS_STEP)
    public Step enrichClubsStep(@Qualifier(ENRICH_STEP_READER) ItemReader<Club> reader,
                                @Qualifier(ENRICH_STEP_PROCESSOR) ItemProcessor<Club, Club> processor,
                                @Qualifier(ENRICH_STEP_WRITER) ItemWriter<Club> writer) {
        return new StepBuilder(READ_AND_PERSIST_STEP, jobRepository)
                .<Club, Club>chunk(chunkSize, platformTransactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean(name = ENRICH_STEP_READER)
    protected ItemReader<Club> clubItemReader(EntityManagerFactory entityManagerFactory) throws Exception {
        return new JpaCursorItemReaderBuilder<Club>()
                .name(ENRICH_STEP_READER)
                .entityManagerFactory(entityManagerFactory)
                .queryString(QUERY_CLUBS)
                .build();
    }

    @Bean(name = ENRICH_STEP_PROCESSOR)
    public ItemProcessor<Club, Club> clubItemProcessor() {
        return item -> {
            String url = basePropertyRestApiUrl + String.format("clubs/%s/profile", item.getId());
            return BatchUtils.getItem(url, Club.class, restTemplate);
        };
    }


    @Bean(name = ENRICH_STEP_WRITER)
    public ItemWriter<Club> clubItemWriter() {
        return items -> items.forEach(f -> {
            clubRepository.save(f);
        });
    }

    private String getSeason() {
        LocalDate currentDate = LocalDate.now();
        int startYear;
        int endYear;
        if (currentDate.getMonthValue() >= 7) {
            startYear = currentDate.getYear();
            endYear = startYear + 1;
        } else {
            endYear = currentDate.getYear();
            startYear = endYear - 1;
        }
        return String.format("%d/%d", startYear, endYear);
    }

}
