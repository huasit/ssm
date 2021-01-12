package com.huasit.ssm.business.specimen.entity;

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
    Integer findStudyTimingByUserId(Long userId);

    /**
     *
     */
    @Modifying
    @Query("update SpecimenStudy set studyTiming=studyTiming+?1 where userId=?2 and specimenId=?3")
    void updateStudyTiming(int interval, Long userId, Long specimenId);

    /**
     *
     */
    @Query(nativeQuery = true,value = "select b.id,b.name,ifnull(a.study_timing,0) study_timing,c.color from (SELECT specimen_id,sum(study_timing) study_timing FROM `specimen_study` where user_id=?1 group by specimen_id) a left join specimen b on a.specimen_id=b.id left join specimen_bank c on b.bank_id=c.id where b.del=0 and c.del=0 order by study_timing desc,b.name")
    List<Object[]> findTiming(Long userId);
}