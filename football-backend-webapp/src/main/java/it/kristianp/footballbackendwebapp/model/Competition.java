package it.kristianp.footballbackendwebapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;

@Getter
@Setter
@Entity
@DynamicUpdate
@DynamicInsert
@ToString
@Table(name = Competition.TABLE_NAME)
public class Competition implements Serializable {

    public static final String TABLE_NAME = "COMPETITION";

    @Id
    @NotNull
    @Column(length = 20, nullable = false)
    private String id;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "country")
    private String country;

    @NotNull
    @Column(name = "clubs")
    private String clubs;

    @NotNull
    @Column(name = "players")
    private String players;

    @NotNull
    @Column(name = "total_market_value")
    private String totalMarketValue;

    @NotNull
    @Column(name = "mean_market_value")
    private String meanMarketValue;

    @NotNull
    @Column(name = "continent")
    private String continent;
}
