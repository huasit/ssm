package com.huasit.ssm.core.role.entity;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

/**
 *
 */
@Transactional
public interface RoleUserRepository extends CrudRepository<RoleUser, Long>, JpaSpecificationExecutor<RoleUser>  {

    /**
     *
     */
    @Modifying
    @Query("delete from RoleUser where roleId=?1")
    void deleteByRoleId(Long roleId);
}