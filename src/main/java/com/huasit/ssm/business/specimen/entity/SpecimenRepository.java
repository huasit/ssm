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
public interface SpecimenRepository extends CrudRepository<Specimen, Long>, JpaSpecificationExecutor<Specimen>  {

    /**
     *
     */
    @Query("from Specimen where code=?1 and del=0")
    Specimen findByCode(String code);

    /**
     *
     */
    @Modifying
    @Query("update Specimen set del=?1 where id=?2")
    void updateDel(boolean del, Long id);
}