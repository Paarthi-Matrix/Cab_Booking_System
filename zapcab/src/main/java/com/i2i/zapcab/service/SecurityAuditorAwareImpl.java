package com.i2i.zapcab.service;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * <p>
 *     This class provides the current auditor (user) information for auditing purposes.
 *     It implements the AuditorAware interface, which is used by Spring Data JPA to obtain
 *     the current auditor (user) for auditing fields like createdBy and lastModifiedBy.
 * </p>
 */
@Component
public class SecurityAuditorAwareImpl implements AuditorAware<String> {

    /**
     * <p>
     *     This method retrieves the current Authentication object from the SecurityContextHolder
     *     and extracts the username of the authenticated user. If no user is authenticated, it returns
     *     an empty Optional.
     * </p>
     *
     * @return An Optional containing the username of the authenticated user, or an empty Optional if no user
     *         is authenticated.
     */
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null == authentication || !authentication.isAuthenticated()) {
            return Optional.empty();
        }
        return Optional.of(authentication.getName());
    }
}
