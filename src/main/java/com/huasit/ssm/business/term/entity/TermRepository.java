package com.huasit.ssm.business.term.entity;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

/**
 *
 */
@Transactional
public interface TermRepository extends CrudRepository<Term, Long>, JpaSpecificationExecutor<Term>  {

    /**
     *
     */
    List<Term> findAll();

    /**
     *
     */
    @Query("from Term where year=?1 order by dateFrom asc")
    List<Term> findByYear(int year);

    /**
     *
     */
    @Query("from Term where year=?1 and num=?2")
    Term findByYearAndNum(int year, int num);
}