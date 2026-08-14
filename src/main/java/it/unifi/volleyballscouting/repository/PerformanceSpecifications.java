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
    public static Specification<Performance> forSet(Set set){
        return (((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("set"), set)));
    }
    public static Specification<Performance> forMatch(Match match){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("set").get("match"), match));
    }

    //------------stat threshold filtering--------------

    public static Specification<Performance> minAces(int minAces){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.ge(root.get("aces"), minAces));
    }
    public static Specification<Performance> minServeErrors(int minErrors){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.ge(root.get("serveErrors"), minErrors));
    }
    public static Specification<Performance> minAttacksGood(int minAttacks){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.ge(root.get("attacksGood"), minAttacks));
    }
    public static Specification<Performance> minAttacksBad(int minErrors){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.ge(root.get("attacksBad"), minErrors));
    }
    public static Specification<Performance> minBlocksGood(int minBlocks){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.ge(root.get("blocksGood"), minBlocks));
    }
    public static Specification<Performance> minBlocksBad(int minErrors){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.ge(root.get("blocksBad"), minErrors));
    }
    public static Specification<Performance> minReceiveGood(int minReceive){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.ge(root.get("receiveGood"), minReceive));
    }
    public static Specification<Performance> minReceiveBad(int minErrors){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.ge(root.get("receiveBad"), minErrors));
    }
}
