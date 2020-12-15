package com.huasit.ssm.core.role.entity;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

/**
 *
 */
@Transactional
public interface RoleRepository extends CrudRepository<Role, Long>, JpaSpecificationExecutor<Role>  {

    /**
     *
     */
    @Query("from Role where sign=?1")
    Role findBySign(String sign);

    /**
     *
     */
    @Modifying
    @Query("update Role set del=?1 where id=?2")
    void updateDel(boolean del, Long id);

    /**
     *
     */
    @Query("from Role r where r.del=false and r.id in (select roleId from RoleUser where userId=?1)")
    List<Role> findRolesByUserId(Long userId);
}