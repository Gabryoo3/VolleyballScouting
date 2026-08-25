package it.unifi.volleyballscouting.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class PerformanceId implements Serializable {
    private UUID playerId;
    private UUID setId;

    public PerformanceId() {}

    public PerformanceId(UUID playerId, UUID setId) {
        this.playerId = playerId;
        this.setId = setId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PerformanceId that)) return false;
        return Objects.equals(playerId, that.playerId) && Objects.equals(setId, that.setId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerId, setId);
    }
}

