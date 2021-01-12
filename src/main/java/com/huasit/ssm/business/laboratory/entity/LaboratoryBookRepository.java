package com.huasit.ssm.business.laboratory.entity;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.Date;

/**
 *
 */
@Transactional
public interface LaboratoryBookRepository extends CrudRepository<LaboratoryBook, Long>, JpaSpecificationExecutor<LaboratoryBook>  {

    /**
     *
     */
    @Modifying
    @Query("update LaboratoryBook set del=?1 where id=?2")
    void updateDel(boolean del, Long id);

    /**
     *
     */
    @Modifying
    @Query("update LaboratoryBook set teacherId=?1 where id=?2")
    void updateTeacherId(Long teacherId, Long id);

    /**
     *
     */
    @Modifying
    @Query("update LaboratoryBook set teacherId2=?1 where id=?2")
    void updateTeacherId2(Long teacherId2, Long id);

    /**
     *
     */
    @Query("select l from LaboratoryBook l where l.del=false and l.userId=?1 and l.bookDay=?2 and (l.bookHour=?3 or l.bookHour2=?3)")
    LaboratoryBook findByUserAndTime(Long userId, Date day, int hour);

    /**
     *
     */
    @Query("select count(l.id) from LaboratoryBook l where l.del=false and l.userId=?1 and l.bookDay=?2 and (l.bookHour=?3 or l.bookHour2=?3)")
    int findBookCountByUser(Long userId, Date day, int hour);

    /**
     *
     */
    @Query("select count(l.id) from LaboratoryBook l where l.id not in ?1 and l.del=false and l.userId=?2 and l.bookDay=?3 and (l.bookHour=?4 or l.bookHour2=?4)")
    int findBookCountByUser(Long id, Long userId, Date day, int hour);

    /**
     *
     */
    @Query("select count(l.id) from LaboratoryBook l where l.del=false and l.laboratory.id=?1 and l.bookDay=?2 and (l.bookHour=?3 or l.bookHour2=?3)")
    int findBookCountByLaboratory(Long laboratoryId, Date day, int hour);

    /**
     *
     */
    @Query("select count(l.id) from LaboratoryBook l where l.id not in ?1 and l.del=false and l.laboratory.id=?2 and l.bookDay=?3 and (l.bookHour=?4 or l.bookHour2=?4)")
    int findBookCountByLaboratory(Long id, Long laboratoryId, Date day, int hour);
}