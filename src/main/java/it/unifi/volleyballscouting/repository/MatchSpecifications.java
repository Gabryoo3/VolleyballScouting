package it.unifi.volleyballscouting.repository;

import it.unifi.volleyballscouting.model.Match;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class MatchSpecifications {

    protected MatchSpecifications(){}

    //--------------team filtering------------------
    public static Specification<Match> forTeam(UUID teamId){
        return (root, query, cb) -> cb.or(
                cb.equal(root.get("teamHome").get("id"), teamId),
                cb.equal(root.get("teamHome").get("id"), teamId));
    }

    //-------------date filtering--------------------
    public static Specification<Match> fromDate(LocalDate date){
        return ((root, query, cb) -> cb.greaterThanOrEqualTo(
                root.<LocalDateTime>get("matchDateTime"), date.atStartOfDay()));
    }

    public static Specification<Match> toDate(LocalDate date) {
        return ((root, query, cb) -> cb.lessThanOrEqualTo(
                root.<LocalDateTime>get("matchDateTime"), date.plusDays(1).atStartOfDay()));
    }

    //--------------location filtering---------------------
    public static Specification<Match> forCity(String city){
        return ((root, query, cb) -> cb.like(
                cb.lower(root.get("address").get("city")), "%" + city.toLowerCase() + "%"));
    }

    //----------------------result filtering----------------
    public static Specification<Match> forWin(UUID teamId){
        return (root, query, cb) -> cb.or(
                cb.and(cb.equal(root.get("teamHome").get("id"), teamId), cb.greaterThan(root.<Integer>get("homeScore"), root.<Integer>get("guestScore"))),
                cb.and(cb.equal(root.get("teamGuest").get("id"), teamId), cb.greaterThan(root.<Integer>get("guestScore"), root.<Integer>get("homeScore")))
        );
    }

    public static Specification<Match> forLose(UUID teamId) {
        return (root, query, cb) -> cb.or(
                cb.and(cb.equal(root.get("teamHome").get("id"), teamId), cb.lessThan(root.<Integer>get("homeScore"), root.<Integer>get("guestScore"))),
                cb.and(cb.equal(root.get("teamGuest").get("id"), teamId), cb.lessThan(root.<Integer>get("guestScore"), root.<Integer> get("homeScore")))
        );
    }
}
