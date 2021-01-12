package com.huasit.ssm.business.attendance.entity;

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
public interface AttendanceRepository extends CrudRepository<Attendance, Long>, JpaSpecificationExecutor<Attendance>  {

    /**
     *
     */
    @Query("from Attendance where token=?1")
    Attendance findByToken(String token);

    /**
     *
     */
    @Query("from Attendance where date=?1 and laboratoryId=?2")
    Attendance findByDateAndLaboratoryId(Date date,  Long laboratoryId);

    /**
     *
     */
    @Modifying
    void save(AttendanceUser arg1);
    /**
     *
     */
    @Query("from AttendanceUser where userId=?3 and attendanceId in (select id from Attendance where date=?1 and laboratoryId=?2)")
    AttendanceUser findByDateAndLaboratoryIdAndUserId(Date date, Long laboratoryId, Long userId);

    /**
     *
     */
    @Query("from AttendanceUser where attendanceId=?1 and userId=?2")
    AttendanceUser findByAttendanceIdAndUserId(Long attendanceId,  Long userId);
}