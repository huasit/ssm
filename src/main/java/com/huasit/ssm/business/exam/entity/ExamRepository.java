package com.huasit.ssm.business.exam.entity;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

/**
 *
 */
@Transactional
public interface ExamRepository extends CrudRepository<Exam, Long>, JpaSpecificationExecutor<Exam>  {

    /**
     *
     */
    @Query("from Exam where term.id=?1")
    Exam findByTermId(Long termId);
}