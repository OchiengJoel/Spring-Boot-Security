package com.joe.springsec.auth.config;


import com.joe.springsec.auth.jwt.AuthEntryPointJwt;
import com.joe.springsec.auth.jwt.AuthTokenFilter;
import com.joe.springsec.auth.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Autowired
    private RateLimiterInterceptor rateLimiterInterceptor;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

            http.csrf(csrf -> csrf.disable())
                    .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                    .sessionManagement(session -> session
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authorizeHttpRequests(auth -> auth
                            .antMatchers("/api/auth/signin/**").permitAll()
                            .antMatchers("/api/admin/**").hasRole("ADMIN")
                            .antMatchers("/api/company/**").hasAnyRole("ADMIN", "MODERATOR")
                            .antMatchers("/api/user/**").hasRole("USER")
                            .antMatchers("/api/admin/**").hasAuthority("ADMIN") // Check for ADMIN authority
                            .antMatchers("/api/company/**").hasAnyAuthority("ADMIN", "MODERATOR") // Check for ADMIN or MODERATOR authority
                            .antMatchers("/api/user/**").hasAuthority("VIEW_USER") // Specific permission for VIEW_USER
                            .antMatchers("/api/user/**").hasAuthority("ADD_USER")
                            .antMatchers("/api/auth/signup/**").hasAuthority("ADD_USER")

                            .anyRequest().authenticated()
                    );
            http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
            return http.build();
        }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rateLimiterInterceptor)
                .addPathPatterns("/api/auth/signin");  // Apply only to the /signin endpoint
    }
    }


//        http.csrf(csrf -> csrf.disable())
//                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
//                .sessionManagement(session -> session
//                        // Keep using stateless session, JWT token manages session-like behavior
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeHttpRequests(auth -> auth
//                        .antMatchers("/api/auth/**").permitAll()
//                        .antMatchers("/api/test/**").permitAll()
//                        .anyRequest().authenticated()
//                );
//
//        http.authenticationProvider(authenticationProvider());
//
//        // Add JWT token filter before username and password authentication filter
//        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

//        http.csrf(csrf -> csrf.disable())
//                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeHttpRequests(auth -> auth
//                        .antMatchers("/api/auth/**").permitAll()
//                        .antMatchers("/api/admin/**").hasRole("ADMIN")
//                        .antMatchers("/api/company/**").hasAnyRole("ADMIN", "MODERATOR")
//                        .antMatchers("/api/user/**").hasRole("USER")
//                        .anyRequest().authenticated()
//                );
//
//        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
//        return http.build();




