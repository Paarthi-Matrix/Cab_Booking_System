package com.i2i.zapcab.service;

import com.i2i.zapcab.helper.RoleEnum;
import com.i2i.zapcab.model.Role;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *     An interface that manages the {@link Role} entity operations
 * </p>
 */
@Component
public interface RoleService {
    List<Role> getByRoleType(List<RoleEnum> roleEnums);
}
