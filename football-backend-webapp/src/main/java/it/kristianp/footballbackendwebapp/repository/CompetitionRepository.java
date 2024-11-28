package it.kristianp.footballbackendwebapp.repository;

import it.kristianp.footballbackendwebapp.model.Competition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CompetitionRepository extends JpaRepository<Competition, String> {

    @Query("SELECT c FROM Competition c " +
            "WHERE (:freeText IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :freeText, '%')))")
    Page<Competition> searchByFreeText(@Param("freeText") String freeText, Pageable pageable);
}
