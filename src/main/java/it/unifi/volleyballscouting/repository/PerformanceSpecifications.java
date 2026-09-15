package it.unifi.volleyballscouting.repository;

import it.unifi.volleyballscouting.model.*;
import org.springframework.data.jpa.domain.Specification;

public class PerformanceSpecifications {

    protected PerformanceSpecifications(){}

    //-------------subject filtering------------------
    public static Specification<Performance> forPlayer(Player player){
        return (root, query, cb) -> cb.equal(root.get("player"), player);
    }
    public static Specification<Performance> forTeam (Team team){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("player").get("team"), team));
    }
    public static Specification<Performance> forSet(GameSet gameSet){
        return (((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("gameSet"), gameSet)));
    }
    public static Specification<Performance> forMatch(Match match){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("gameSet").get("match"), match));
    }

    //------------stat threshold filtering--------------

    public static Specification<Performance> minAttackPoint(int min){ return (r,q,cb) -> cb.ge(r.get("attackPoint"), min); }
    public static Specification<Performance> minAttackError(int min){ return (r,q,cb) -> cb.ge(r.get("attackError"), min); }
    public static Specification<Performance> minServePoint(int min){ return (r,q,cb) -> cb.ge(r.get("servePoint"), min); }
    public static Specification<Performance> minServeError(int min){ return (r,q,cb) -> cb.ge(r.get("serveError"), min); }
    public static Specification<Performance> minBlockPoint(int min){ return (r,q,cb) -> cb.ge(r.get("blockPoint"), min); }
    public static Specification<Performance> minBlockError(int min){ return (r,q,cb) -> cb.ge(r.get("blockError"), min); }
    public static Specification<Performance> minDigGood(int min){ return (r,q,cb) -> cb.ge(r.get("digGood"), min); }
    public static Specification<Performance> minDigError(int min){ return (r,q,cb) -> cb.ge(r.get("digError"), min); }
    public static Specification<Performance> minReceiveGood(int min){ return (r,q,cb) -> cb.ge(r.get("receiveGood"), min); }
    public static Specification<Performance> minReceiveError(int min){ return (r,q,cb) -> cb.ge(r.get("receiveError"), min); }
}
