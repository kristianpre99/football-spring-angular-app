package it.kristianp.footballbackendwebapp.hibernate;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CustomPhysicalNamingStrategy extends PhysicalNamingStrategyStandardImpl {

    @Value("${tables.prefix:FB_}")
    public String tableNamePrefix;

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
        Identifier newIdentifier = new Identifier(tableNamePrefix + name.getText(), name.isQuoted());
        return super.toPhysicalTableName(newIdentifier, context);
    }
}


