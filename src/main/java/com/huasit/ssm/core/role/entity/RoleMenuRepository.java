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
public interface RoleMenuRepository extends CrudRepository<RoleMenu, Long>, JpaSpecificationExecutor<RoleMenu>  {

    /**
     *
     */
    @Modifying
    @Query("delete from RoleMenu where roleId=?1")
    void deleteByRoleId(Long roleId);
}