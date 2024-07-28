package com.i2i.zapcab.helper;

import org.springframework.security.core.context.SecurityContextHolder;

import com.i2i.zapcab.exception.AuthenticationException;

/**
 * <p>
 * This method is responsible for decoding the JWT token and return the preferred values from it.
 * </p>
 */
public class JwtDecoder {

    /**
     * <p>
     *     This method is responsible for fetching username of
     *     the currently authenticated user from the filter.
     *     The securityContextHolder is set when the respective api hits for authentication,
     *     which sets the username in principle of SecurityContextHolder.
     * </p>
     * <p>
     *     IMPORTANT NOTE:
     * </p>
     * <ul>
     *     <li>The SecurityContextHolder of Spring 3 is used</li>
     *     <li>The behaviour of this method may differ if the Spring version is changed.</li>
     * </ul>
     *
     * @throws AuthenticationException
     *         If the user is not authenticated, that user will not have a SecurityContextHolder.
     *         That will pay a way to raise this exception.
     * @return
     *         UserId of the currently authenticated user.
     *
     */
    public static String extractUserIdFromToken() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal != null) {
            return ((org.springframework.security.core.userdetails.User) principal).getUsername();
        }
        throw new AuthenticationException("Unable to retrieve user details from security context");
    }
}
