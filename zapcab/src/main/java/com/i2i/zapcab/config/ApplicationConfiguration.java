package com.i2i.zapcab.config;

import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import com.i2i.zapcab.exception.NotFoundException;
import com.i2i.zapcab.model.User;
import com.i2i.zapcab.repository.UserRepository;

/**
 * <p>
 * The ApplicationConfiguration class is a configuration class for setting up the application's security components.
 * This class is responsible for configuring user details service, authentication provider, authentication manager,
 * and password encoder. It ensures that user authentication and authorization are handled appropriately by integrating
 * with Spring Security and the application's user repository.
 * </p>
 * <p>
 * Key Responsibilities:
 * </p>
 * <ul>
 *     <li>Define and provide a UserDetailsService bean that loads user-specific data during authentication.</li>
 *     <li>Configure an AuthenticationProvider bean to process authentication requests using the defined UserDetailsService
 *         and password encoder.</li>
 *     <li>Provide an AuthenticationManager bean that manages authentication processes in the application.</li>
 *     <li>Define and provide a PasswordEncoder bean for encoding passwords using the BCrypt hashing algorithm.</li>
 * </ul>
 *
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfiguration {

    @Autowired
    private UserRepository userRepository;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Optional<User> user = userRepository.findById(username);
            if (ObjectUtils.isEmpty(user)) {
                throw new NotFoundException("User not found");
            }
            return new org.springframework.security.core.userdetails.User(
                    user.get().getId(),
                    user.get().getPassword(),
                    user.get().getRole().stream()
                            .map(role -> new SimpleGrantedAuthority(role.getRoleName().name()))
                            .collect(Collectors.toList())
            );
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}