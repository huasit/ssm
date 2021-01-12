package com.huasit.ssm.business.teacher.entity;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

/**
 *
 */
@Transactional
public interface TeacherScoreRepository extends CrudRepository<TeacherScore, Long>, JpaSpecificationExecutor<TeacherScore>  {

    /**
     *
     */
    @Query("from TeacherScore where bookId=?1 and bookHour=?2 and creatorId=?3")
    TeacherScore findByBookIdAndBookHourAndCreatorId(Long bookId, int bookHour, Long creatorId);
}