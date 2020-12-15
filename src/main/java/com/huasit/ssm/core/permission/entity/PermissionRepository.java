package com.huasit.ssm.core.permission.entity;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

/**
 *
 */
@Transactional
public interface PermissionRepository extends CrudRepository<Permission, Long>, JpaSpecificationExecutor<Permission>  {

    /**
     *
     */
    @Query("select p from Permission p where p.del=false and (p.id in (select permissionId from PermissionRole where roleId in (select id from Role where del=false) and roleId in (select roleId from RoleUser where userId=?1)) or p.id in (select permissionId from PermissionUser where userId=?1))")
    List<Permission> findPermissionsByUserId(Long userId);
}