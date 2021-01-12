package com.huasit.ssm.business.exam.entity;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

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

    /**
     *
     */
    @Query("from Exam order by id desc")
    List<Exam> findLastExam(Pageable pageable);
}