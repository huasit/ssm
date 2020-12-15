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
public interface QuestionRepository extends CrudRepository<Question, Long>, JpaSpecificationExecutor<Question>  {

    /**
     *
     */
    @Modifying
    @Query("update Question set del=?1 where id=?2")
    void updateDel(boolean del, Long id);
    /**
     *
     */
    @Modifying
    @Query("update Question set del=?1 where bankId=?2")
    void updateDelByBankId(boolean del, Long bankId);

    /**
     *
     */
    @Query("from Question where del=false and bankId=?1")
    List<Question> findByBankId(Long bankId);
}