package com.huasit.ssm.business.question.entity;

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
public interface QuestionBankRepository extends CrudRepository<QuestionBank, Long>, JpaSpecificationExecutor<QuestionBank>  {

    /**
     *
     */
    @Query("from QuestionBank")
    List<QuestionBank> findAll();

    /**
     *
     */
    @Query("from QuestionBank where del=false and id in (select bankId from Question where del=false)")
    List<QuestionBank> findWhichHasQuestions();


    /**
     *
     */
    @Modifying
    @Query("update QuestionBank set del=?1 where id=?2")
    void updateDel(boolean del, Long id);
}