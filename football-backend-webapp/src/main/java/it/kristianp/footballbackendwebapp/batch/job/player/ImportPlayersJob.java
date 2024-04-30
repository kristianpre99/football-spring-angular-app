package it.kristianp.footballbackendwebapp.batch.job.player;

import it.kristianp.footballbackendwebapp.batch.job.player.domain.PlayerResponse;
import it.kristianp.footballbackendwebapp.batch.job.util.BatchUtils;
import it.kristianp.footballbackendwebapp.model.Club;
import it.kristianp.footballbackendwebapp.properties.BatchProperties;
import it.kristianp.footballbackendwebapp.repository.PlayerRepository;
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

@Slf4j
@RequiredArgsConstructor
@Configuration(ImportPlayersJob.NAME + ".config")
public class ImportPlayersJob {
    public static final String NAME = "importPlayersJob";
    private static final String JOB_PREFIX = NAME + ".";
    private static final String QUERY_PLAYERS = "select c from Club c where c.id not in (select p.club.id from Player p)";

    private static final String GET_PLAYERS_BY_CLUB_AND_PERSIST = "getPlayersByClubAndPersist";
    private static final String GET_PLAYERS_BY_CLUB_AND_PERSIST_STEP = JOB_PREFIX + GET_PLAYERS_BY_CLUB_AND_PERSIST + ".step";
    private static final String GET_PLAYERS_READER = GET_PLAYERS_BY_CLUB_AND_PERSIST_STEP + ".reader";
    private static final String GET_PLAYERS_PROCESSOR = GET_PLAYERS_BY_CLUB_AND_PERSIST_STEP + ".processor";
    private static final String GET_PLAYERS_WRITER = GET_PLAYERS_BY_CLUB_AND_PERSIST_STEP + ".writer";

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final PlayerRepository playerRepository;
    private final BatchProperties batchProperties;

    @Value("${base.transfermarkt.rest.api.url}")
    private String basePropertyRestApiUrl;
    @Value("${importPlayersJob.chunk.size:10}")
    private Integer chunkSize;

    @Bean(name = NAME)
    public Job job(
            @Qualifier(GET_PLAYERS_BY_CLUB_AND_PERSIST_STEP) Step getPlayersByClubAndPersist
    ) {
        return new JobBuilder(NAME, jobRepository)
                .start(getPlayersByClubAndPersist)
                .build();
    }

    @Bean(name = GET_PLAYERS_BY_CLUB_AND_PERSIST_STEP)
    public Step readAndPersist(@Qualifier(GET_PLAYERS_READER) ItemReader<Club> reader,
                               @Qualifier(GET_PLAYERS_PROCESSOR) ItemProcessor<Club, PlayerResponse> processor,
                               @Qualifier(GET_PLAYERS_WRITER) ItemWriter<PlayerResponse> writer) {
        return new StepBuilder(GET_PLAYERS_BY_CLUB_AND_PERSIST_STEP, jobRepository)
                .<Club, PlayerResponse>chunk(chunkSize, platformTransactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean(name = GET_PLAYERS_READER)
    protected ItemReader<Club> reader(EntityManagerFactory entityManagerFactory) throws Exception {
        JpaCursorItemReader<Club> jpaCursorItemReader = new JpaCursorItemReaderBuilder<Club>()
                .name(GET_PLAYERS_READER)
                .entityManagerFactory(entityManagerFactory)
                .queryString(QUERY_PLAYERS)
                .build();

        if (batchProperties.isLimited()) {
            jpaCursorItemReader.setMaxItemCount(3);
        }
        return jpaCursorItemReader;
    }

    @Bean(name = GET_PLAYERS_PROCESSOR)
    public ItemProcessor<Club, PlayerResponse> processor(RestTemplate restTemplate) {
        return item -> {
            String url = basePropertyRestApiUrl + String.format("clubs/%s/players", item.getId());
            PlayerResponse playerResponse = BatchUtils.getItem(url, PlayerResponse.class, restTemplate);
            if (playerResponse == null) {
                return null;
            }
            playerResponse.setClub(item);
            return playerResponse;
        };
    }

    @Bean(name = GET_PLAYERS_WRITER)
    public ItemWriter<PlayerResponse> writer() {
        return items -> items.forEach(p -> {
            Club club = p.getClub();
            if (p.getPlayers() != null) {
                p.getPlayers().forEach(f -> f.setClub(club));
                playerRepository.saveAll(p.getPlayers());
            }
        });
    }

}
