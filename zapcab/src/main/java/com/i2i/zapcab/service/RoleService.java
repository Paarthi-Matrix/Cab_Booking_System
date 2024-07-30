package com.i2i.zapcab.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.i2i.zapcab.helper.RoleEnum;
import com.i2i.zapcab.model.Role;


/**
 * <p>
 * An interface that manages the {@link Role} entity operations
 * </p>
 */
@Component
public interface RoleService {
    /**
     * <p>
     * Used to get the list of Role based on role type.
     * </p>
     *
     * @param roleEnums
     * @return
     */
    List<Role> getByRoleType(List<RoleEnum> roleEnums);
}