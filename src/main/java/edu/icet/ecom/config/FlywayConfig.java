package edu.icet.ecom.config;

import org.flywaydb.core.Flyway;
import org.springframework.boot.flyway.autoconfigure.FlywayMigrationInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {

    /**
     * Custom FlywayMigrationInitializer that runs repair() before migrate()
     * to automatically fix any failed migration entries in the schema history table.
     */
    @Bean
    public FlywayMigrationInitializer flywayInitializer(Flyway flyway) {
        return new FlywayMigrationInitializer(flyway, f -> {
            f.repair();
            f.migrate();
        });
    }
}


