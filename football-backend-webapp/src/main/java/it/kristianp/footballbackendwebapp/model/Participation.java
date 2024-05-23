package it.kristianp.footballbackendwebapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
@FieldNameConstants
@Entity
@Table(name = Participation.TABLE_NAME)
public class Participation {
    public static final String TABLE_NAME = "PARTICIPATION";

    @EmbeddedId
    private Id id = new Id();

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    @MapsId(Id.Fields.CLUB_ID)
    @JoinColumn(name = Id.Columns.CLUB_ID, nullable = false)
    private Club club;

    @NotNull
    @ManyToOne
    @MapsId(Id.Fields.COMPETITION_ID)
    @JoinColumn(name = Id.Columns.COMP_ID, nullable = false)
    private Competition competition;

    @Column(name = "season")
    private String season;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_date")
    private Date modifyDate;

    @Embeddable
    @Data
    @FieldNameConstants
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Id implements Serializable {

        @Column(name = Columns.CLUB_ID, nullable = false)
        private Long clubId;

        @Column(name = Columns.COMP_ID, nullable = false)
        private String competitionId;

        public static final class Columns {

            public static final String CLUB_ID = "club_id";
            public static final String COMP_ID = "competition_id";

            private Columns() {
                throw new UnsupportedOperationException("A constants class cannot be instantiate, use static fields");
            }

        }
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Participation that = (Participation) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(id);
    }
}
