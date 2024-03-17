package commons;

import commons.primaryKeys.DebtKey;
import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
public class Debt {
    @Transient
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long tempId;
    @EmbeddedId
    private DebtKey debtKey;

    @ManyToOne
    @MapsId("eventId")
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "debtor_event_id", referencedColumnName = "event_id"),
        @JoinColumn(name = "debtor_id", referencedColumnName = "participant_id")})
    private Participant debtor;

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "creditor_event_id", referencedColumnName = "event_id"),
        @JoinColumn(name = "creditor_id", referencedColumnName = "participant_id")})
    private Participant creditor;

    @Column(name = "amount")
    private double amount;

    @SuppressWarnings("unused")
    protected Debt() {
        // for object mapper
    }

    public Debt(Event event, Participant debtor, Participant creditor, double amount) {
        this.debtKey = new DebtKey(event.getId(), tempId);
        this.event = event;
        this.debtor = debtor;
        this.creditor = creditor;
        this.amount = amount;
    }

    public DebtKey getDebtKey() {
        return debtKey;
    }

    public Event getEvent() {
        return event;
    }

    public long getEventId() {
        return debtKey.getEventId();
    }

    public long getId() {
        return debtKey.getId();
    }

    public Participant getDebtor() {
        return debtor;
    }

    public void setDebtor(Participant debtor) {
        this.debtor = debtor;
    }

    public Participant getCreditor() {
        return creditor;
    }

    public void setCreditor(Participant creditor) {
        this.creditor = creditor;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }
}
