package it.kristianp.footballbackendwebapp.repository;

import it.kristianp.footballbackendwebapp.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource
public interface PlayerRepository extends JpaRepository<Player, Long> {
}
