package it.kristianp.footballbackendwebapp.repository;

import it.kristianp.footballbackendwebapp.model.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {
//public interface ClubRepository extends BaseJpaRepository<Club, Long> {
}
