package it.kristianp.footballbackendwebapp.batch.job.player.domain;

import it.kristianp.footballbackendwebapp.model.Club;
import it.kristianp.footballbackendwebapp.model.Competition;
import it.kristianp.footballbackendwebapp.model.Player;
import lombok.Data;

import java.util.List;

@Data
public class PlayerResponse {

    private List<Player> players;
    private Club club;
}
