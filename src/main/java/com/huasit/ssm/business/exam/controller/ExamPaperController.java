package com.huasit.ssm.business.exam.controller;

import com.huasit.ssm.business.exam.entity.ExamPaper;
import com.huasit.ssm.business.exam.service.ExamPaperService;
import com.huasit.ssm.business.exam.service.ExamService;
import com.huasit.ssm.system.locale.Response;
import com.huasit.ssm.system.security.model.AuthenticationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/exam/paper/")
public class ExamPaperController {

    /**
     *
     */
    @ResponseBody
    @GetMapping("/list/")
    public ResponseEntity<Map<String, Object>> list(ExamPaper form, @RequestParam("page") int page, @RequestParam(name = "pageSize", defaultValue = "10") int pageSize, Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        Page<ExamPaper> examPapers = this.examPaperService.list(form, page, pageSize, loginUser.getSources());
        return Response.success("list", examPapers.getContent(), "page", page, "count", examPapers.getTotalElements(), "total_page", examPapers.getTotalPages()).entity();
    }

    /**
     *
     */
    @Autowired
    ExamService examService;

    /**
     *
     */
    @Autowired
    ExamPaperService examPaperService;
}