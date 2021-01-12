package com.huasit.ssm.business.laboratory.entity;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

/**
 *
 */
@Transactional
public interface LaboratoryOpeningRepository extends CrudRepository<LaboratoryOpening, Long>, JpaSpecificationExecutor<LaboratoryOpening>  {

    /**
     *
     */
    @Query("from LaboratoryOpening where laboratoryId=?1 and day like ?2")
    List<LaboratoryOpening> findByLaboratoryIdAndDate(Long laboratoryId, String date);

    /**
     *
     */
    @Query("from LaboratoryOpening where laboratoryId=?1 and day=?2")
    LaboratoryOpening findByLaboratoryIdAndDay(Long laboratoryId, String day);
}