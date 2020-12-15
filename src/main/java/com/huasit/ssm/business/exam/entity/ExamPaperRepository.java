package com.huasit.ssm.business.exam.entity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 *
 */
@Transactional
public interface ExamPaperRepository extends CrudRepository<ExamPaper, Long>, JpaSpecificationExecutor<ExamPaper>  {

    /**
     *
     */
    @Query("from ExamPaper e where e.userId=?1 and e.examId=?2 and e.submitTime is null and e.startTime>=?3")
    Page<ExamPaper> findUnCompleteByUserIdAndExamId(Long userId, Long examId, Date overCheckDate, Pageable pageable);

    /**
     *
     */
    @Query("from ExamPaper where userId=?1 and examId=?2")
    List<ExamPaper> findByUserIdAndExamId(Long userId, Long examId);
}