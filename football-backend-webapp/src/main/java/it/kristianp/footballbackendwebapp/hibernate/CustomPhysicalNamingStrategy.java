package it.kristianp.footballbackendwebapp.hibernate;

import it.kristianp.footballbackendwebapp.properties.FootballAppConfigProperties;
import lombok.RequiredArgsConstructor;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CustomPhysicalNamingStrategy extends PhysicalNamingStrategyStandardImpl {

    private final FootballAppConfigProperties footballAppConfigProperties;

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
        Identifier newIdentifier = new Identifier(footballAppConfigProperties.getHibernateTablePrefix() + name.getText(), name.isQuoted());
        return super.toPhysicalTableName(newIdentifier, context);
    }
}


