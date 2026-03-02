package com.example.product_api.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@ConditionalOnProperty(value = "app.schema.patch.enabled", havingValue = "true", matchIfMissing = true)
public class SchemaPatchConfig {

    @Bean
    CommandLineRunner patchProductImagesColumn(JdbcTemplate jdbcTemplate) {
        return args -> {
            try {
                jdbcTemplate.execute("ALTER TABLE tb_produto MODIFY COLUMN images LONGTEXT NULL");
                System.out.println("✅ Schema patch aplicado: tb_produto.images -> LONGTEXT");
            } catch (Exception ex) {
                System.out.println("ℹ️  Schema patch ignorado (tb_produto/images indisponível no contexto atual)");
            }
        };
    }
}
