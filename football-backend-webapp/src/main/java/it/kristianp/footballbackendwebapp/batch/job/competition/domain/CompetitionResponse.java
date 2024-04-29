package it.kristianp.footballbackendwebapp.batch.job.competition.domain;

import it.kristianp.footballbackendwebapp.model.Competition;
import lombok.Data;

import java.util.List;

@Data
public class CompetitionResponse {

    private List<Competition> results;
}
