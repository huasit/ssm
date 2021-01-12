package com.huasit.ssm.business.attendance.service;

import com.huasit.ssm.business.attendance.entity.Attendance;
import com.huasit.ssm.business.attendance.entity.AttendanceRepository;
import com.huasit.ssm.business.attendance.entity.AttendanceUser;
import com.huasit.ssm.core.user.entity.User;
import com.huasit.ssm.system.exception.SystemError;
import com.huasit.ssm.system.exception.SystemException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class AttendanceService {

    /**
     *
     */
    @Resource
    AttendanceRepository attendanceRepository;

    /**
     *
     */
    public Attendance newAttendance(Long laboratoryId, String dateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(dateStr);
        Attendance attendance = this.attendanceRepository.findByDateAndLaboratoryId(date, laboratoryId);
        if (attendance != null) {
            return attendance;
        }
        attendance = new Attendance();
        attendance.setDate(date);
        attendance.setLaboratoryId(laboratoryId);
        attendance.setToken(UUID.randomUUID().toString());
        this.attendanceRepository.save(attendance);
        return attendance;
    }

    /**
     *
     */
    public void applyAttendance(String token, User loginUser) {
        Attendance attendance = this.attendanceRepository.findByToken(token);
        if (attendance == null) {
            throw new SystemException(SystemError.FORMDATA_ERROR);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (!sdf.format(attendance.getDate()).equals(sdf.format(new Date()))) {
            throw new SystemException(SystemError.ATTENDANCE_APPLY_NOT_TODAY);
        }
        AttendanceUser au = this.attendanceRepository.findByAttendanceIdAndUserId(attendance.getId(), loginUser.getId());
        if(au != null) {
            return;
        }
        au = new AttendanceUser();
        au.setAttendanceId(attendance.getId());
        au.setUserId(loginUser.getId());
        au.setCreateTime(new Date());
        this.attendanceRepository.save(au);
    }

    /**
     *
     */
    public boolean userIsAttendance(Date date, Long laboratoryId, Long userId) {
        AttendanceUser au = this.attendanceRepository.findByDateAndLaboratoryIdAndUserId(date, laboratoryId, userId);
        return au != null;
    }
}
