package it.unifi.volleyballscouting.query;

import it.unifi.volleyballscouting.model.Match;
import it.unifi.volleyballscouting.repository.MatchSpecifications;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class MatchQuery {
    private final UUID team1Id;
    private final UUID team2Id;
    private final LocalDate from;
    private final LocalDate to;
    private final String city;

    private MatchQuery(Builder b){
        this.team1Id = b.team1Id; this.team2Id = b.team2Id; this.city = b.city;
        LocalDate start = b.from; LocalDate end = b.to;
        if (start != null && end != null && start.isAfter(end)){
            LocalDate temp = start;
            start = end;
            temp = end;
        }

        this.from = start;
        this.to = end;

    }

    public static class Builder{
        private UUID team1Id; private UUID team2Id; private LocalDate from; private LocalDate to; private String city;

        public Builder team1Id(UUID team1Id) { this.team1Id = team1Id; return this; }
        public Builder team2Id(UUID team2Id) { this.team2Id = team2Id; return this; }
        public Builder from(LocalDate from) { this.from = from; return this; }
        public Builder to(LocalDate to) { this.to = to; return this; }
        public Builder city(String city) { this.city = (city == null || city.isBlank()) ? null : city.trim(); return this;}
        public MatchQuery build(){
            return new MatchQuery(this);
        }
    }

    public Specification<Match> toSpecification(){
        return Stream.of(
                Optional.ofNullable(team1Id).map(MatchSpecifications::forTeam),
                Optional.ofNullable(team2Id).map(MatchSpecifications::forTeam),
                Optional.ofNullable(from).map(MatchSpecifications::fromDate),
                Optional.ofNullable(to).map(MatchSpecifications::toDate),
                Optional.ofNullable(city).map(MatchSpecifications::forCity)
        ).filter(Optional::isPresent).map(Optional::get).reduce(Specification.allOf(), Specification::and);
    }


}
