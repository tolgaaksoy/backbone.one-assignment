package one.backbone.messagingassignment.configuration;

import jakarta.servlet.http.HttpServletResponse;
import one.backbone.messagingassignment.security.jwt.AuthEntryPointJwt;
import one.backbone.messagingassignment.security.jwt.AuthTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * The type Web security config.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    private final AuthTokenFilter authTokenFilter;
    private final AuthEntryPointJwt unauthorizedHandler;

    private final UserDetailsService userDetailsService;

    public WebSecurityConfig(AuthTokenFilter authTokenFilter,
                             AuthEntryPointJwt unauthorizedHandler,
                             UserDetailsService userDetailsService) {
        this.authTokenFilter = authTokenFilter;
        this.unauthorizedHandler = unauthorizedHandler;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Authentication manager authentication manager.
     *
     * @param authConfig the auth config
     * @return the authentication manager
     * @throws Exception the exception
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Password encoder password encoder.
     *
     * @return the password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Filter chain security filter chain.
     *
     * @param http the http
     * @return the security filter chain
     * @throws Exception the exception
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(new AntPathRequestMatcher("/auth/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/websocket/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/websocket/ws/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/api/websocket/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/api/websocket/ws/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/ws/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/api/ws/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/v3/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
                                .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(headers -> headers.frameOptions().sameOrigin())
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout.logoutUrl("/auth/logout").logoutSuccessHandler((request, response, authentication) -> {
                    SecurityContextHolder.clearContext();
                    response.setStatus(HttpServletResponse.SC_OK);
                }));

        return http.build();
    }
}
