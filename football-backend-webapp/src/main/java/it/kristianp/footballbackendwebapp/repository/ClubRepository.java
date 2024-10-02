package it.kristianp.footballbackendwebapp.repository;

import it.kristianp.footballbackendwebapp.model.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {

    @Query(value = "select c.* from fb_club c " +
            "join fb_participation p on c.id = p.club_id " +
            "join fb_competition co on co.id = p.competition_id " +
            "where p.competition_id = :competitionId",
            nativeQuery = true)
    List<Club> findClubNamesByCompetitionId(String competitionId);
}
