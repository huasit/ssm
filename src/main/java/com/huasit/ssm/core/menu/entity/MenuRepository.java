package com.huasit.ssm.core.menu.entity;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

/**
 *
 */
@Transactional
public interface MenuRepository extends CrudRepository<Menu, Long>, JpaSpecificationExecutor<Menu>  {


    /**
     *
     */
    @Query("from Menu where del=false order by orderIndex asc")
    List<Menu> findAll();


    /**
     *
     */
    @Query("from Menu m where m.del=false and m.id in (select menuId from RoleMenu where roleId in (select id from Role where del=false) and roleId in (select roleId from RoleUser where userId=?1)) order by m.orderIndex asc")
    List<Menu> findMenusByUserId(Long userId);
}