package com.i2i.zapcab.config;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import static com.i2i.zapcab.common.ZapCabConstant.AUTHORIZATION_HEADER;
import static com.i2i.zapcab.common.ZapCabConstant.BEARER_HEADER;

/**
 * JwtAuthenticationFilter is responsible for filtering incoming requests to
 * ensure they contain valid JWT tokens. It performs the necessary authentication
 * and authorization checks, setting the authentication context if the token is valid.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LogManager.getLogger(JwtAuthenticationFilter.class);
    @Autowired
    JwtService jwtService;
    @Autowired
    UserDetailsService userDetailsService;

    /**
     * <p>
     * Filters each incoming request to check for a valid JWT token and sets the
     * authentication context if the token is valid.
     * </p>
     * <p>
     * NOTE:
     * </p>
     * <ul>
     *     <li>The incoming header should contain "Authorization" : "Bearer <Your-JWT token>"</li>
     * </ul>
     *
     * @param request     The HTTP request.
     * @param response    The HTTP response.
     * @param filterChain The filter chain.
     * @throws ServletException If an error occurs during the filtering process.
     * @throws IOException      If an I/O error occurs during the filtering process.
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String jwt = "";
        logger.debug("Invoking JWT filter.");
        if (request.getServletPath().contains("/v1/auth")) {
            logger.debug("Hit endpoint for auth. Passing to next filter chain.");
            filterChain.doFilter(request, response);
            return;
        }
        final String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        final String userId;
        if (authHeader == null || !authHeader.startsWith(BEARER_HEADER)) {
            logger.warn("No proper headers for HttpServletRequest. Passing to next filter chain.");
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(BEARER_HEADER.length());
        userId = jwtService.extractUsername(jwt);
        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userId);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
                logger.debug("User Authenticated successfully and added to the SecurityContextHolder");
            } else {
                //todo exception
                logger.warn("The JWT for the user is not valid. Either the user's JWT is invalid or" +
                        " the user got deleted(soft deleted)");

            }
        }
        if (isAuthorized(request)) {
            logger.info("The request authorized");
            filterChain.doFilter(request, response);
        } else {
            logger.info("The request is not authorized");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    /**
     * <p>
     * Checks if the current authenticated user is authorized to access the requested path.
     * </p>
     * <p>
     * IMPORTANT NOTE!
     * As per the requirements of the project on JUL 2024 the following
     * aspects are considered during authorization.
     * </p>
     * <ul>
     *     <li>DRIVER have authority to both CUSTOMER and DRIVER</li>
     *     <li>CUSTOMER have authority to CUSTOMER only</li>
     *     <li>ADMIN have all authorities</li>
     * </ul>
     *
     * @param request The HTTP request.
     * @return True if the user is authorized, false otherwise.
     */
    private boolean isAuthorized(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        List<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        Map<String, List<String>> allowedPaths = new HashMap<>();
        allowedPaths.put("DRIVER", List.of("/v1/drivers/**", "/v1/customers/**"));
        allowedPaths.put("CUSTOMER", List.of("/v1/customers/**"));
        allowedPaths.put("ADMIN", List.of("/v1/admins/**", "/v1/drivers/**", "/v1/customers/**"));
        String requestPath = request.getServletPath();
        for (Map.Entry<String, List<String>> entry : allowedPaths.entrySet()) {
            String role = entry.getKey();
            List<String> allowedPathsForRole = entry.getValue();
            for (String pathPattern : allowedPathsForRole) {
                if (new AntPathMatcher().match(pathPattern, requestPath) && roles.contains(role)) {
                    return true;
                }
            }
        }
        return false;
    }
}