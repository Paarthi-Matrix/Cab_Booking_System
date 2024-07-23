package com.i2i.zapcab.repository;

import com.i2i.zapcab.helper.RoleEnum;
import com.i2i.zapcab.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    @Query("SELECT r FROM Role r WHERE r.roleName IN :roleEnum")
    List<Role> findByRoleType(@Param("roleEnum") List<RoleEnum> roleEnum);
}
