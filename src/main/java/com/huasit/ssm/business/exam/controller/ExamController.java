package com.huasit.ssm.business.exam.controller;

import com.huasit.ssm.business.exam.entity.Exam;
import com.huasit.ssm.business.exam.entity.ExamPaper;
import com.huasit.ssm.business.exam.entity.ExamPaperQuestion;
import com.huasit.ssm.business.exam.service.ExamService;
import com.huasit.ssm.system.exception.SystemError;
import com.huasit.ssm.system.exception.SystemException;
import com.huasit.ssm.system.locale.Response;
import com.huasit.ssm.system.security.model.AuthenticationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/exam")
public class ExamController {

    /**
     *
     */
    @ResponseBody
    @GetMapping("/in/")
    public ResponseEntity<Map<String, Object>> in(Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        Exam exam = this.examService.getExamWithCreate(loginUser.getSources());
        ExamPaper paper = this.examService.getLastUnCompleteExamPaper(exam, loginUser.getSources());
        if (paper == null) {
            return Response.success("exam", exam).entity();
        }
        this.examService.shuffleQuestion(paper);
        return Response.success("exam", exam, "paper", paper).entity();
    }

    /**
     *
     */
    @ResponseBody
    @GetMapping("/start/")
    public ResponseEntity<Map<String, Object>> start(@RequestParam Long examId, Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        Exam exam = this.examService.getExamById(examId);
        if (exam == null) {
            throw new SystemException(SystemError.EXAM_DATA_ERROR);
        }
        ExamPaper paper = this.examService.startExam(exam, loginUser.getSources());
        this.examService.shuffleQuestion(paper);
        return Response.success("exam", exam, "paper", paper).entity();
    }

    /**
     *
     */
    @ResponseBody
    @PostMapping("/answer/")
    public ResponseEntity<Map<String, Object>> answer(@RequestBody ExamPaperQuestion form, Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        this.examService.answerAQuestion(form, loginUser.getSources());
        return Response.success("success", true).entity();
    }

    /**
     *
     */
    @ResponseBody
    @PostMapping("/submit/")
    public ResponseEntity<Map<String, Object>> submit(@RequestBody ExamPaper form, Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        this.examService.submitExam(form, loginUser.getSources());
        return Response.success("success", true).entity();
    }

    /**
     *
     */
    @Autowired
    ExamService examService;
}