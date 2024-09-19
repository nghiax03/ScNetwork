package com.nghianguyen.scnetwork.config;

//import com.nghianguyen.scnetwork.Utils.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Value("${api.prefix}")
    private String apiPrefix;

//    @Autowired
//    private JwtTokenFilter jwtTokenFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests ->
                        requests.requestMatchers(String.format("%s/users/register", apiPrefix))
                                .permitAll()
                                .requestMatchers(String.format("%s/users/login", apiPrefix))
                                .permitAll()
                                .requestMatchers(String.format("%s/posts/**", apiPrefix)).permitAll()
                                .anyRequest().authenticated())
                .build();
    }
}
