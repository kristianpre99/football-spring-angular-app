package it.kristianp.footballbackendwebapp.model;

import it.kristianp.footballbackendwebapp.model.base.auditable.AuditableModel;
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
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Getter
@Setter
@Entity
@DynamicUpdate
@DynamicInsert
@ToString
@Table(name = Competition.TABLE_NAME)
public class Competition extends AuditableModel {

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

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Competition that = (Competition) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
