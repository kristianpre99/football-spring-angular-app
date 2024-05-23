package it.kristianp.footballbackendwebapp.model;

import it.kristianp.footballbackendwebapp.model.base.BaseModel;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Set;

@Getter
@Setter
@FieldNameConstants
@ToString
@DynamicUpdate
@DynamicInsert
@Entity
@Table(name = Club.TABLE_NAME)
public class Club extends BaseModel {

    public static final String TABLE_NAME = "CLUB";

    @Column(name = "name")
    private String name;

    @Column(name = "image")
    private String image;

    @Column(name = "address_line1")
    private String addressLine1;

    @Column(name = "address_line2")
    private String addressLine2;

    @Column(name = "address_line3")
    private String addressLine3;

    @Column(name = "website")
    private String website;

    @Column(name = "founded_on")
    private String foundedOn;

    @ToString.Exclude
    @Basic(fetch = FetchType.LAZY)
    @ElementCollection
    @CollectionTable(name = "club_colors", joinColumns = @JoinColumn(name = "club_id"))
    @Column(name = "color")
    private Set<String> colors;

    @Column(name = "stadium_name")
    private String stadiumName;

    @Column(name = "stadium_seats")
    private String stadiumSeats;

    @Column(name = "current_transfer_record")
    private String currentTransferRecord;

    @Column(name = "current_market_value")
    private String currentMarketValue;

    @Embedded
    private Squad squad;

    @ToString.Exclude
    @OneToMany(mappedBy = "club")
    private Set<Player> players;


    @Data
    @Embeddable
    public static class Squad {
        @Column(name = "size")
        private String size;

        @Column(name = "average_age")
        private String averageAge;

        @Column(name = "foreigners")
        private String foreigners;

        @Column(name = "national_team_players")
        private String nationalTeamPlayers;
    }
}
