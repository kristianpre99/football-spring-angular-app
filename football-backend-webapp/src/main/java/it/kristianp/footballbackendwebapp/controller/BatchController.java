package it.kristianp.footballbackendwebapp.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/v1/batch")
public class BatchController {

    private final JobLauncher jobLauncher;
    private final Job jobCompetitions;
    private final Job jobClubs;
    private final Job jobPlayers;

    public BatchController(JobLauncher jobLauncher,
                           @Qualifier("importCompetitionsJob") Job jobCompetitions,
                           @Qualifier("importClubsJob") Job jobClubs,
                           @Qualifier("importPlayersJob") Job jobPlayers
    ) {
        this.jobLauncher = jobLauncher;
        this.jobCompetitions = jobCompetitions;
        this.jobClubs = jobClubs;
        this.jobPlayers = jobPlayers;
    }

    @PostMapping("/importCompetitions")
    public void importCompetitions() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis())
                .toJobParameters();
        try {
            jobLauncher.run(jobCompetitions, jobParameters);
        } catch (JobExecutionAlreadyRunningException
                 | JobRestartException
                 | JobInstanceAlreadyCompleteException
                 | JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/importClubs")
    public void importClubs() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis())
                .toJobParameters();
        try {
            jobLauncher.run(jobClubs, jobParameters);
        } catch (JobExecutionAlreadyRunningException
                 | JobRestartException
                 | JobInstanceAlreadyCompleteException
                 | JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/importPlayers")
    public void importPlayers() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis())
                .toJobParameters();
        try {
            jobLauncher.run(jobPlayers, jobParameters);
        } catch (JobExecutionAlreadyRunningException
                 | JobRestartException
                 | JobInstanceAlreadyCompleteException
                 | JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }
}
