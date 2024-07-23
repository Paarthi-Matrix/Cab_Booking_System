package com.i2i.zapcab.service;

import com.i2i.zapcab.helper.RoleEnum;
import com.i2i.zapcab.model.Role;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface RoleService {
    public List<Role> getByRoleType(List<RoleEnum> roleEnums);
}
