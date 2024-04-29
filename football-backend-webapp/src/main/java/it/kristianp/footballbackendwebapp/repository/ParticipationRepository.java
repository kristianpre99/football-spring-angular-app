package it.kristianp.footballbackendwebapp.repository;

import it.kristianp.footballbackendwebapp.model.Participation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipationRepository extends JpaRepository<Participation, Long> {
}
