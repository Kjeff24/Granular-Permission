package com.bexos.granular_permission;

import com.bexos.granular_permission.models.RoleEnum;
import com.bexos.granular_permission.models.User;
import com.bexos.granular_permission.repositories.UserRepository;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@RequiredArgsConstructor
public class GranularPermissionApplication {
    private final PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(GranularPermissionApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(UserRepository userRepository) {

        return args -> createUser(userRepository);
    }

    void createUser(UserRepository userRepository) {
        if (userRepository.count() == 0) {
            User admin = User.builder()
                    .role(RoleEnum.ADMIN)
                    .username("admin")
                    .password(passwordEncoder.encode("password"))
                    .email("admin@gmail.com")
                    .build();
            userRepository.save(admin);

            User coAdmin = User.builder()
                    .role(RoleEnum.CO_ADMIN)
                    .username("co-admin")
                    .password(passwordEncoder.encode("password"))
                    .email("co-admin@gmail.com")
                    .manager(admin)
                    .referrer(admin)
                    .build();
            userRepository.save(coAdmin);

            User organizer = User.builder()
                    .role(RoleEnum.ORGANIZER)
                    .username("organizer")
                    .password(passwordEncoder.encode("password"))
                    .email("organizer@gmail.com")
                    .build();
            userRepository.save(organizer);

            User coOrganizer = User.builder()
                    .role(RoleEnum.CO_ORGANIZER)
                    .username("co-Organizer")
                    .password(passwordEncoder.encode("password"))
                    .email("co-organizer@gmail.com")
                    .manager(organizer)
                    .referrer(organizer)
                    .build();
            userRepository.save(coOrganizer);

            for (int i = 1; i < 5; i++) {
                Faker faker = new Faker();
                String firstName = faker.name().firstName();
                String lastName = faker.name().lastName();
                String email = (firstName + "." + lastName + "@gmail.com");
                var user = User.builder()
                        .email(email)
                        .username(firstName + " " + lastName)
                        .password(passwordEncoder.encode("password"))
                        .role(RoleEnum.ATTENDEE)
                        .build();
                userRepository.save(user);
            }
        }
    }

}
