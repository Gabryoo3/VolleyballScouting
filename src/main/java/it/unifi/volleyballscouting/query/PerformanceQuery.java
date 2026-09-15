package it.unifi.volleyballscouting.query;

import it.unifi.volleyballscouting.model.*;
import it.unifi.volleyballscouting.repository.PerformanceSpecifications;
import org.springframework.data.jpa.domain.Specification;
import java.util.Optional;
import java.util.stream.Stream;

public class PerformanceQuery {
    private final Player player;
    private final Team team;
    private final GameSet gameSet;
    private final Match match;
    private final Integer minAttackPoint, minAttackError;
    private final Integer minServePoint, minServeError;
    private final Integer minBlockPoint, minBlockError;
    private final Integer minDigGood, minDigError;
    private final Integer minReceiveGood, minReceiveError;

    private PerformanceQuery(Builder b){
        this.player = b.player; this.team = b.team; this.gameSet = b.gameSet; this.match = b.match;
        this.minAttackPoint = b.minAttackPoint; this.minAttackError = b.minAttackError;
        this.minServePoint = b.minServePoint;   this.minServeError = b.minServeError;
        this.minBlockPoint = b.minBlockPoint;   this.minBlockError = b.minBlockError;
        this.minDigGood = b.minDigGood;         this.minDigError = b.minDigError;
        this.minReceiveGood = b.minReceiveGood; this.minReceiveError = b.minReceiveError;
    }

    public Specification<Performance> toSpecification(){
        return Stream.of(
                        Optional.ofNullable(player).map(PerformanceSpecifications::forPlayer),
                        Optional.ofNullable(team).map(PerformanceSpecifications::forTeam),
                        Optional.ofNullable(gameSet).map(PerformanceSpecifications::forSet),
                        Optional.ofNullable(match).map(PerformanceSpecifications::forMatch),
                        Optional.ofNullable(minAttackPoint).map(PerformanceSpecifications::minAttackPoint),
                        Optional.ofNullable(minAttackError).map(PerformanceSpecifications::minAttackError),
                        Optional.ofNullable(minServePoint).map(PerformanceSpecifications::minServePoint),
                        Optional.ofNullable(minServeError).map(PerformanceSpecifications::minServeError),
                        Optional.ofNullable(minBlockPoint).map(PerformanceSpecifications::minBlockPoint),
                        Optional.ofNullable(minBlockError).map(PerformanceSpecifications::minBlockError),
                        Optional.ofNullable(minDigGood).map(PerformanceSpecifications::minDigGood),
                        Optional.ofNullable(minDigError).map(PerformanceSpecifications::minDigError),
                        Optional.ofNullable(minReceiveGood).map(PerformanceSpecifications::minReceiveGood),
                        Optional.ofNullable(minReceiveError).map(PerformanceSpecifications::minReceiveError)
                )
                .filter(Optional::isPresent).map(Optional::get)
                .reduce(Specification.allOf(), Specification::and);
    }

    public static class Builder{
        private Player player; private Team team; private GameSet gameSet; private Match match;
        private Integer minAttackPoint, minAttackError, minServePoint, minServeError,
                minBlockPoint, minBlockError, minDigGood, minDigError, minReceiveGood, minReceiveError;

        public Builder player(Player player){ this.player = player; return this; }
        public Builder team(Team team){ this.team = team; return this; }
        public Builder set(GameSet gameSet){ this.gameSet = gameSet; return this; }
        public Builder match(Match match){ this.match = match; return this; }
        public Builder minAttackPoint(Integer v){ this.minAttackPoint = v; return this; }
        public Builder minAttackError(Integer v){ this.minAttackError = v; return this; }
        public Builder minServePoint(Integer v){ this.minServePoint = v; return this; }
        public Builder minServeError(Integer v){ this.minServeError = v; return this; }
        public Builder minBlockPoint(Integer v){ this.minBlockPoint = v; return this; }
        public Builder minBlockError(Integer v){ this.minBlockError = v; return this; }
        public Builder minDigGood(Integer v){ this.minDigGood = v; return this; }
        public Builder minDigError(Integer v){ this.minDigError = v; return this; }
        public Builder minReceiveGood(Integer v){ this.minReceiveGood = v; return this; }
        public Builder minReceiveError(Integer v){ this.minReceiveError = v; return this; }

        public PerformanceQuery build(){
            if (player == null && gameSet == null && match == null && team == null)
                throw new IllegalArgumentException("Almeno un filtro è richiesto");
            return new PerformanceQuery(this);
        }
    }
}