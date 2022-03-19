package neo.springsecurityinmemory;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@SpringBootApplication
public class SpringSecurityInmemoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityInmemoryApplication.class, args);
    }

    @Bean UserDetailsManager userDetailsService() {
        return new InMemoryUserDetailsManager();
    }

    @Bean InitializingBean initializer(UserDetailsManager manager) {
        return () -> {
            UserDetails uRahul = User.withDefaultPasswordEncoder()
                    .username("rahul")
                    .password("pass")
                    .roles("USER")
                    .build();
            UserDetails uPawan = User
					.withUserDetails(uRahul)
					.username("pawan")
					.build();
			manager.createUser(uRahul);
            manager.createUser(uPawan);
        };
    }

}

@RestController
class GreetingRestController {

    @GetMapping("/greeting")
    String greeting(Principal principal) {
        return "Hello," + principal.getName();
    }
}

@Configuration
@EnableWebSecurity
class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic();
        http
                .authorizeHttpRequests().anyRequest().authenticated();
    }
}
