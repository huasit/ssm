package com.huasit.ssm.business.duty.entity;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 *
 */
@Transactional
public interface DutyRepository extends CrudRepository<Duty, Long>, JpaSpecificationExecutor<Duty>  {

    /**
     *
     */
    @Query("from Duty where laboratoryId=?1 and date=?2")
    Duty findByDate(Long laboratoryId, Date date);

    /**
     *
     */
    @Query("from Duty where laboratoryId=?1 and date>=?2 and date<=?3")
    List<Duty> findByDate(Long laboratoryId, Date dateFrom, Date dateTo);

    /**
     *
     */
    @Modifying
    @Query("delete from Duty where id=?1")
    void delete(Long id);
}