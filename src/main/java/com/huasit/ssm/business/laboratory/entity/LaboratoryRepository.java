package com.huasit.ssm.business.laboratory.entity;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

/**
 *
 */
@Transactional
public interface LaboratoryRepository extends CrudRepository<Laboratory, Long>, JpaSpecificationExecutor<Laboratory>  {

    /**
     *
     */
    @Modifying
    @Query("update Laboratory set del=?1 where id=?2")
    void updateDel(boolean del, Long id);
}