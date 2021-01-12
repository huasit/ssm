package com.huasit.ssm.business.attendance.controller;

import com.huasit.ssm.business.attendance.entity.Attendance;
import com.huasit.ssm.business.attendance.service.AttendanceService;
import com.huasit.ssm.system.locale.Response;
import com.huasit.ssm.system.security.model.AuthenticationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/attendance")
public class AttendanceController {

    /**
     *
     */
    @Autowired
    AttendanceService attendanceService;

    /**
     *
     */
    @ResponseBody
    @GetMapping("/code/")
    public ResponseEntity<Map<String, Object>> code(Long laboratoryId, String date, Authentication authentication) throws Exception {
        Attendance attendance = this.attendanceService.newAttendance(laboratoryId, date);
        return Response.success("attendance", attendance).entity();
    }

    /**
     *
     */
    @ResponseBody
    @GetMapping("/apply/")
    public ResponseEntity<Map<String, Object>> apply(String token, Authentication authentication) throws Exception {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        this.attendanceService.applyAttendance(token, loginUser.getSources());
        return Response.success("success", true).entity();
    }
}
