package it.kristianp.footballbackendwebapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
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
    @MapsId(Id.Fields.clubId)
    @JoinColumn(name = Id.Columns.CLUB_ID, nullable = false)
    private Club club;

    @NotNull
    @ManyToOne
    @MapsId(Id.Fields.competitionId)
    @JoinColumn(name = Id.Columns.COMP_ID, nullable = false)
    private Competition competition;

    @Column(name = "season")
    private String season;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_date")
    private Date modifyDate;

    public Participation() {

    }

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

}
