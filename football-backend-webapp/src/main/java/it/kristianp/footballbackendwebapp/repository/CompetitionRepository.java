package it.kristianp.footballbackendwebapp.repository;

import it.kristianp.footballbackendwebapp.model.Competition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompetitionRepository extends JpaRepository<Competition, String> {
}
