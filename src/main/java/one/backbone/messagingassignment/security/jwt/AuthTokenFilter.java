package one.backbone.messagingassignment.security.jwt;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import one.backbone.messagingassignment.repository.TokenRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * The type Auth token filter.
 */
@Slf4j
@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    private final TokenRepository tokenRepository;

    private final JwtUtils jwtUtils;

    private final UserDetailsService userDetailsService;

    public AuthTokenFilter(TokenRepository tokenRepository,
                           JwtUtils jwtUtils,
                           UserDetailsService userDetailsService) {
        this.tokenRepository = tokenRepository;
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = jwtUtils.parseJwt(request);
            boolean isTokenValid = tokenRepository.findByAccessToken(jwt)
                    .map(t -> !t.isExpired() && !t.isRevoked())
                    .orElse(false);
            if (jwt != null && jwtUtils.validateJwtToken(jwt) && isTokenValid) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            log.error("Cannot set user authentication:", e);
            log.error("Cause: ", e.getCause());
            log.error("Message: {}", e.getMessage());
            log.error("StackTrace: ", (Object[]) e.getStackTrace());
        }

        filterChain.doFilter(request, response);
    }


}
