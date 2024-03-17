package commons.primaryKeys;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ParticipantKey implements Serializable {
    @Column(name = "event_id")
    private long eventId;

    @Column(name = "participant_id")
    private long id;

    public ParticipantKey(long eventId, long id) {
        this.eventId = eventId;
        this.id = id;
    }

    @SuppressWarnings("unused")
    protected ParticipantKey() {
        // for object mappers
    }

    public long getEventId() {
        return eventId;
    }

    public long getId() {
        return id;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParticipantKey that = (ParticipantKey) o;
        return eventId == that.eventId && id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, id);
    }
}
