package com.huasit.ssm.business.teacher.controller;

import com.huasit.ssm.business.teacher.entity.TeacherScore;
import com.huasit.ssm.business.teacher.service.TeacherScoreService;
import com.huasit.ssm.system.locale.Response;
import com.huasit.ssm.system.security.model.AuthenticationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/teacher/score")
public class TeacherScoreController {

    /**
     *
     */
    @Autowired
    TeacherScoreService teacherScoreService;

    /**
     *
     */
    @ResponseBody
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> get(Long bookId, int bookHour, Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        TeacherScore teacherScore = this.teacherScoreService.get(bookId, bookHour, loginUser.getSources());
        if(teacherScore == null) {
            return Response.success("empty", true).entity();
        }
        return Response.success("teacher_score", teacherScore).entity();
    }


    /**
     *
     */
    @ResponseBody
    @GetMapping("/list/")
    public ResponseEntity<Map<String, Object>> list(TeacherScore form, @RequestParam("page") int page, @RequestParam(name = "pageSize", defaultValue = "10") int pageSize, Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        Page<TeacherScore> SpecimenBanks   = this.teacherScoreService.list(form, page, pageSize, loginUser.getSources());
        return Response.success("list", SpecimenBanks.getContent(), "page", page, "count", SpecimenBanks.getTotalElements(), "total_page", SpecimenBanks.getTotalPages()).entity();
    }

    /**
     *
     */
    @ResponseBody
    @PostMapping("/")
    public ResponseEntity<Map<String, Object>> add(@RequestBody TeacherScore form, Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        this.teacherScoreService.save(form, loginUser.getSources());
        return Response.success("specimen_bank", form).entity();
    }

    /**
     *
     */
    @ResponseBody
    @PutMapping("/{id}/")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody TeacherScore form, Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        this.teacherScoreService.save(form, loginUser.getSources());
        return Response.success("success", true).entity();
    }

    /**
     *
     */
    @ResponseBody
    @DeleteMapping("/{id}/")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id, Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        this.teacherScoreService.delete(id, loginUser.getSources());
        return Response.success("success", true).entity();
    }
}
