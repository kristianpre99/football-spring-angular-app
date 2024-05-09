package it.kristianp.footballbackendwebapp;

import it.kristianp.footballbackendwebapp.auth.model.User;
import it.kristianp.footballbackendwebapp.auth.repository.UserRepository;
import it.kristianp.footballbackendwebapp.properties.FootballAppConfigProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


//@SpringBootApplication(exclude = {
//        SecurityAutoConfiguration.class,
//        ManagementWebSecurityAutoConfiguration.class})
//@EnableJpaRepositories(
//        value = "",
//        repositoryBaseClass = BaseJpaRepositoryImpl.class
//)
@SpringBootApplication
public class FootballBackendWebappApplication {

    public static void main(String[] args) {
        SpringApplication.run(FootballBackendWebappApplication.class, args);
    }

    //    @Bean
    public CommandLineRunner startup(FootballAppConfigProperties footballAppConfigProperties,
                                     UserRepository userRepository) {

        return args -> {
            System.out.println(footballAppConfigProperties);
            for (int i = 0; i < 20; i++) {
                User user = new User();
                user.setFirstName("test");
                user.setLastName("test");
                user.setEmail("test");
                user.setPassword("test");
                user.setRole(User.Role.ADMIN);
                userRepository.save(user);
            }
        };

    }

}
