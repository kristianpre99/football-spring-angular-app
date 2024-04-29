package it.kristianp.footballbackendwebapp.batch.job.club.domain;

import it.kristianp.footballbackendwebapp.model.Club;
import it.kristianp.footballbackendwebapp.model.Competition;
import lombok.Data;

import java.util.List;

@Data
public class ClubResponse {

    private List<Club> clubs;
    private Competition competition;
}
