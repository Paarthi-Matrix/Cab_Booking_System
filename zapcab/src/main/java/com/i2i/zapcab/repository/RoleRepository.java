package com.i2i.zapcab.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.i2i.zapcab.helper.RoleEnum;
import com.i2i.zapcab.model.Role;

import static com.i2i.zapcab.common.ZapCabConstant.FIND_BY_ROLE_TYPE_QUERY;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    @Query(FIND_BY_ROLE_TYPE_QUERY)
    List<Role> findByRoleType(@Param("roleEnum") List<RoleEnum> roleEnum);
}
