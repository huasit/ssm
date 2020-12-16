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
public interface SpecimenStudyRepository extends CrudRepository<SpecimenStudy, Long>, JpaSpecificationExecutor<SpecimenStudy> {

    /**
     *
     */
    @Query("from SpecimenStudy where userId=?1 and specimenId=?2")
    SpecimenStudy findByUserIdAndSpecimenId(Long userId, Long specimenId);

    /**
     *
     */
    @Query("select sum(s.studyTiming) from SpecimenStudy s where s.userId=?1")
    int findStudyTimingByUserId(Long userId);

    /**
     *
     */
    @Modifying
    @Query("update SpecimenStudy set studyTiming=studyTiming+?1 where userId=?2 and specimenId=?3")
    void updateStudyTiming(int interval, Long userId, Long specimenId);
}