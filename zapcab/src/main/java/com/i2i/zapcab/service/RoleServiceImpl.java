package com.i2i.zapcab.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.i2i.zapcab.helper.RoleEnum;
import com.i2i.zapcab.model.Role;
import com.i2i.zapcab.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleRepository roleRepository;
    @Override
    public List<Role> getByRoleType(List<RoleEnum> roleEnums) {
        return roleRepository.findByRoleType(roleEnums);
    }
}
