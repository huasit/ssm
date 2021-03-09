package com.huasit.ssm.business.classes.entity;

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
public interface ClassesRepository extends CrudRepository<Classes, Long>, JpaSpecificationExecutor<Classes> {
    /**
     *
     */
    @Modifying
    @Query("update Classes set del=?1 where id=?2")
    void updateDel(boolean del, Long id);

    List<Classes> findAll();
}