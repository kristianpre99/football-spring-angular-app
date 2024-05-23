package it.kristianp.footballbackendwebapp.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import it.kristianp.footballbackendwebapp.model.base.BaseModel;
import it.kristianp.footballbackendwebapp.model.deserializer.CustomLocalDateDeserializer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@FieldNameConstants
@Entity
@Table(name = Player.TABLE_NAME)
public class Player extends BaseModel {

    public static final String TABLE_NAME = "PLAYER";

    @Column(name = "name")
    private String name;

    @Column(name = "position")
    private String position;

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "age")
    private Integer age;

    @ElementCollection
    @CollectionTable(name = "player_nationality", joinColumns = @JoinColumn(name = "player_id"))
    @Column(name = "nationality")
    private Set<String> nationality;

    @Column(name = "height")
    private String height;

    @Column(name = "foot")
    private String foot;

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    @Column(name = "joined_on")
    private LocalDate joinedOn;

    @Column(name = "signed_from")
    private String signedFrom;

    @Column(name = "contract")
    private String contract;

    @Column(name = "market_value")
    private String marketValue;

    @ManyToOne
    @JoinColumn(name = "club_id", referencedColumnName = "id")
    private Club club;

}