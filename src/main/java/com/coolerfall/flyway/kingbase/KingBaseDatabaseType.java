package com.coolerfall.flyway.kingbase;

import java.sql.Connection;
import java.sql.Types;
import java.util.List;

import org.flywaydb.core.api.ResourceProvider;
import org.flywaydb.core.api.configuration.Configuration;
import org.flywaydb.core.internal.database.base.BaseDatabaseType;
import org.flywaydb.core.internal.database.base.Database;
import org.flywaydb.core.internal.jdbc.JdbcConnectionFactory;
import org.flywaydb.core.internal.jdbc.StatementInterceptor;
import org.flywaydb.core.internal.parser.Parser;
import org.flywaydb.core.internal.parser.ParsingContext;
import org.flywaydb.database.postgresql.PostgreSQLDatabase;
import org.flywaydb.database.postgresql.PostgreSQLParser;

/**
 * Flyway SPI DatabaseType adapter for KingBaseES.
 *
 * <p>KingBaseES is PostgreSQL-compatible, so this adapter extends BaseDatabaseType
 * and delegates database and parser creation to PostgreSQL implementations.
 * It handles the {@code jdbc:kingbase8:} URL format and {@code com.kingbase8.Driver} driver.</p>
 */
public class KingBaseDatabaseType extends BaseDatabaseType {

    @Override
    public String getName() {
        return "KingBase";
    }

    @Override
    public List<String> getSupportedEngines() {
        return List.of(getName(), "KingbaseES");
    }

    @Override
    public int getNullType() {
        return Types.NULL;
    }

    @Override
    public boolean handlesJDBCUrl(String url) {
        return url.startsWith("jdbc:kingbase8:");
    }

    @Override
    public String getDriverClass(String url, ClassLoader classLoader) {
        return "com.kingbase8.Driver";
    }

    @Override
    public boolean handlesDatabaseProductNameAndVersion(
            String databaseProductName, String databaseProductVersion,
            Connection connection) {
        return databaseProductName.toUpperCase().contains("KINGBASE");
    }

    @Override
    public Database createDatabase(Configuration configuration,
            JdbcConnectionFactory jdbcConnectionFactory,
            StatementInterceptor statementInterceptor) {
        return new PostgreSQLDatabase(configuration, jdbcConnectionFactory,
                statementInterceptor);
    }

    @Override
    public Parser createParser(Configuration configuration,
            ResourceProvider resourceProvider, ParsingContext parsingContext) {
        return new PostgreSQLParser(configuration, parsingContext);
    }
}
