package it.kristianp.footballbackendwebapp.repository;

import it.kristianp.footballbackendwebapp.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}
