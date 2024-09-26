package com.nghianguyen.scnetwork.config;

//import com.nghianguyen.scnetwork.Utils.JwtTokenFilter;
import com.nghianguyen.scnetwork.Utils.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Value("${api.prefix}")
    private String apiPrefix;

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Autowired
    private AuthenticationProvider provider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(provider) //add authentication Provider
                .authorizeHttpRequests(requests -> {
                    requests.requestMatchers(String.format("%s/users/register", apiPrefix))
                            .permitAll()
                            .requestMatchers(String.format("%s/users/login", apiPrefix))
                            .permitAll()

                            .requestMatchers("POST",
                                    String.format("%s/posts/**", apiPrefix)).permitAll()
                            .requestMatchers("GET",
                                    String.format("%s/posts/**", apiPrefix)).permitAll()
                            .requestMatchers("PUT",
                                    String.format("%s/posts/**", apiPrefix)).permitAll()
                            .requestMatchers("DELETE",
                                    String.format("%s/posts/**", apiPrefix)).permitAll()

                            .requestMatchers("POST",
                                    String.format("%s/comments/**", apiPrefix)).permitAll()
                            .requestMatchers("GET",
                                    String.format("%s/comments/**", apiPrefix)).permitAll()
                            .requestMatchers("PUT",
                                    String.format("%s/comments/**", apiPrefix)).permitAll()
                            .requestMatchers("DELETE",
                                    String.format("%s/comments/**", apiPrefix)).permitAll()

                            .requestMatchers(String.format("%s/message/**", apiPrefix)).permitAll()
                            .requestMatchers("/index.html", "/static/**", "/ws/**").permitAll()
                            .anyRequest().authenticated();
                }).csrf(AbstractHttpConfigurer::disable);
        http.cors(new Customizer<CorsConfigurer<HttpSecurity>>() {
            @Override
            public void customize(CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer) {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(List.of("*"));
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
                configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
                configuration.setExposedHeaders(List.of("x-auth-token"));
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                httpSecurityCorsConfigurer.configurationSource(source);
            }
        });
        return http.build();
    }
}
