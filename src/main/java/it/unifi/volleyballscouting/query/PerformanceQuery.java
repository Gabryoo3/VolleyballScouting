package it.unifi.volleyballscouting.query;

import it.unifi.volleyballscouting.model.*;
import it.unifi.volleyballscouting.repository.PerformanceSpecifications;
import org.springframework.data.jpa.domain.Specification;
import java.util.Optional;
import java.util.stream.Stream;

public class PerformanceQuery {
    private final Player player;
    private final Team team;
    private final Set set;
    private final Match match;
    private final Integer minAces;
    private final Integer minServeErrors;
    private final Integer minAttacksGood;
    private final Integer minAttacksBad;
    private final Integer minBlocksGood;
    private final Integer minBlocksBad;
    private final Integer minReceivesGood;
    private final Integer minReceivesBad;

    private PerformanceQuery(Builder b){
        this.player = b.player;
        this.team = b.team;
        this.set = b.set;
        this.match = b.match;
        this.minAces = b.minAces;
        this.minServeErrors = b.minServeErrors;
        this.minAttacksGood = b.minAttacksGood;
        this.minAttacksBad = b.minAttacksBad;
        this.minBlocksGood = b.minBlocksGood;
        this.minBlocksBad = b.minBlocksBad;
        this.minReceivesGood = b.minReceivesGood;
        this.minReceivesBad = b.minReceivesBad;
    }

    public Specification<Performance> toSpecification(){
        return Stream.of(
                Optional.ofNullable(player).map(PerformanceSpecifications::forPlayer),
                Optional.ofNullable(team).map(PerformanceSpecifications::forTeam),
                Optional.ofNullable(set).map(PerformanceSpecifications::forSet),
                Optional.ofNullable(match).map(PerformanceSpecifications::forMatch),
                Optional.ofNullable(minAces).map(PerformanceSpecifications::minAces),
                Optional.ofNullable(minServeErrors).map(PerformanceSpecifications::minServeErrors),
                Optional.ofNullable(minAttacksGood).map(PerformanceSpecifications::minAttacksGood),
                Optional.ofNullable(minAttacksBad).map(PerformanceSpecifications::minAttacksBad),
                Optional.ofNullable(minBlocksGood).map(PerformanceSpecifications::minBlocksGood),
                Optional.ofNullable(minBlocksBad).map(PerformanceSpecifications::minBlocksBad),
                Optional.ofNullable(minReceivesGood).map(PerformanceSpecifications::minReceiveGood),
                Optional.ofNullable(minReceivesBad).map(PerformanceSpecifications::minReceiveBad)
        )
        .filter(Optional::isPresent)
        .map(Optional::get)
        .reduce(Specification.allOf(), Specification::and);
    }

    public static class Builder{
        private Player player;
        private Team team;
        private Set set;
        private Match match;
        private Integer minAces;
        private Integer minServeErrors;
        private Integer minAttacksGood;
        private Integer minAttacksBad;
        private Integer minBlocksGood;
        private Integer minBlocksBad;
        private Integer minReceivesGood;
        private Integer minReceivesBad;

        public Builder player(Player player){ this.player = player; return this; }
        public Builder team(Team team){ this.team = team; return this; }
        public Builder set(Set set){ this.set = set; return this; }
        public Builder match(Match match){ this.match = match; return this; }
        public Builder minAces(Integer min){ this.minAces = min; return this; }
        public Builder minServeErrors(Integer min){ this.minServeErrors = min; return this; }
        public Builder minAttacksGood(Integer min){ this.minAttacksGood = min; return this; }
        public Builder minAttacksBad(Integer min){ this.minAttacksBad = min; return this; }
        public Builder minBlocksGood(Integer min){ this.minBlocksGood = min; return this; }
        public Builder minBlocksBad(Integer min){ this.minBlocksBad = min; return this; }
        public Builder minReceivesGood(Integer min){ this.minReceivesGood = min; return this; }
        public Builder minReceivesBad(Integer min){ this.minReceivesBad = min; return this; }

        public PerformanceQuery build(){
            if (player == null && set == null && match == null && team == null)
                throw new IllegalArgumentException("Almeno un filtro è richiesto");
            return new PerformanceQuery(this);
        }

    }

}


