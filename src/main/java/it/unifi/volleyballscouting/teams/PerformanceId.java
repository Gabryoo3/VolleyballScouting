package it.unifi.volleyballscouting.teams;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PerformanceId implements Serializable {
    private Long playerId;
    private Long setId;

    public PerformanceId() {}

    public PerformanceId(Long playerId, Long setId) {
        this.playerId = playerId;
        this.setId = setId;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public Long getSetId() {
        return setId;
    }

    public void setSetId(Long setId) {
        this.setId = setId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PerformanceId)) return false;
        PerformanceId that = (PerformanceId) o;
        return Objects.equals(playerId, that.playerId) && Objects.equals(setId, that.setId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerId, setId);
    }
}

