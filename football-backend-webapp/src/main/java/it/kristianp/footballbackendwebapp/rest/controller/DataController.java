package it.kristianp.footballbackendwebapp.rest.controller;

import it.kristianp.footballbackendwebapp.model.Club;
import it.kristianp.footballbackendwebapp.model.Competition;
import it.kristianp.footballbackendwebapp.repository.ClubRepository;
import it.kristianp.footballbackendwebapp.repository.CompetitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/data")
public class DataController {

    public static final String PAGE_NUMBER_DEFAULT_VALUE = "0";
    public static final String PAGE_SIZE_DEFAULT_VALUE = "10";
    private final CompetitionRepository competitionRepository;
    private final ClubRepository clubRepository;

    @GetMapping(value = "/competitions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<Competition>> getCompetitions(@RequestParam(defaultValue = PAGE_NUMBER_DEFAULT_VALUE) Integer pageNo,
                                                             @RequestParam(defaultValue = PAGE_SIZE_DEFAULT_VALUE) Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, Sort.by(Competition.Fields.NAME).ascending());
        // pass it to repos
        Page<Competition> competitionPage = competitionRepository.findAll(pageRequest);
        return ResponseEntity.of(Optional.of(competitionPage));
    }

    @GetMapping(value = "/clubs/{competitionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Club>> getClubsByCompetition(@PathVariable String competitionId) {
        List<Club> clubNamesByCompetitionId = clubRepository.findClubNamesByCompetitionId(competitionId);
        return ResponseEntity.of(Optional.of(clubNamesByCompetitionId));
    }
}
