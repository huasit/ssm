package com.huasit.ssm.business.specimen.entity;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

/**
 *
 */
@Transactional
public interface SpecimenBankRepository extends CrudRepository<SpecimenBank, Long>, JpaSpecificationExecutor<SpecimenBank>  {

    /**
     *
     */
    @Modifying
    @Query("update SpecimenBank set del=?1 where id=?2")
    void updateDel(boolean del, Long id);
}