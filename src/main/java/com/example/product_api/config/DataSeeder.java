package com.example.product_api.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.product_api.model.User;
import com.example.product_api.repository.UserRepository;

import java.time.Instant;

/**
 * Data Seeder - Similar ao Seeder do Laravel
 * Popula o banco com dados iniciais quando a aplicação inicia
 */
@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository) {
        return args -> {
            // Verifica se o usuário admin já existe
            if (!userRepository.existsByUsername("admin")) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(encoder.encode("123456")); // Senha criptografada
                admin.setCreatedAt(Instant.now());
                admin.setUpdatedAt(Instant.now());
                
                userRepository.save(admin);
                
                System.out.println("✅ Usuário admin criado com sucesso!");
                System.out.println("   Username: admin");
                System.out.println("   Password: 123456");
            } else {
                System.out.println("ℹ️  Usuário admin já existe no banco de dados");
            }
        };
    }
}
