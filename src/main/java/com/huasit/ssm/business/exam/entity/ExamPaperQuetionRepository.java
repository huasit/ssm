package com.huasit.ssm.business.exam.entity;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

/**
 *
 */
@Transactional
public interface ExamPaperQuetionRepository extends CrudRepository<ExamPaperQuestion, Long>, JpaSpecificationExecutor<ExamPaperQuestion>  {

    /**
     *
     */
    @Query("from ExamPaperQuestion where paperId=?1")
    List<ExamPaperQuestion> findByPaperId(Long paperId);
}