package com.huasit.ssm.business.question.controller;

import com.huasit.ssm.business.question.entity.Question;
import com.huasit.ssm.business.question.entity.QuestionBank;
import com.huasit.ssm.business.question.service.QuestionBankService;
import com.huasit.ssm.system.locale.Response;
import com.huasit.ssm.system.security.model.AuthenticationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Controller
@RequestMapping("/question")
public class QuestionController {

    /**
     *
     */
    @ResponseBody
    @GetMapping("/{id}/")
    public ResponseEntity<Map<String, Object>> get(@PathVariable Long id, Authentication authentication) {
        Question question = this.questionBankService.getQuestionById(id);
        return Response.success("question", question).entity();
    }

    /**
     *
     */
    @ResponseBody
    @GetMapping("/list/")
    public ResponseEntity<Map<String, Object>> list(Question form, @RequestParam("page") int page, @RequestParam(name = "pageSize", defaultValue = "10") int pageSize, Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        Page<Question> questions = this.questionBankService.list(form, page, pageSize, loginUser.getSources());
        return Response.success("list", questions.getContent(), "page", page, "count", questions.getTotalElements(), "total_page", questions.getTotalPages()).entity();
    }

    /**
     *
     */
    @ResponseBody
    @PostMapping("/")
    public ResponseEntity<Map<String, Object>> add(@RequestBody @Valid Question form, Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        this.questionBankService.add(form, loginUser.getSources());
        return Response.success("success", true).entity();
    }

    /**
     *
     */
    @ResponseBody
    @PutMapping("/{id}/")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody @Valid Question form, Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        this.questionBankService.update(id, form, loginUser.getSources());
        return Response.success("success", true).entity();
    }

    /**
     *
     */
    @ResponseBody
    @DeleteMapping("/{id}/")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id, Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        this.questionBankService.delete(id, loginUser.getSources());
        return Response.success("success", true).entity();
    }

    /**
     *
     */
    @ResponseBody
    @GetMapping("/bank/{id}/")
    public ResponseEntity<Map<String, Object>> bankGet(@PathVariable Long id, Authentication authentication) {
        QuestionBank questionBank = this.questionBankService.getQuestionBankById(id);
        return Response.success("question_bank", questionBank).entity();
    }

    /**
     *
     */
    @ResponseBody
    @GetMapping("/bank/list/")
    public ResponseEntity<Map<String, Object>> bankList(QuestionBank form, @RequestParam("page") int page, @RequestParam(name = "pageSize", defaultValue = "10") int pageSize, Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        Page<QuestionBank> questionBanks = this.questionBankService.listBank(form, page, pageSize, loginUser.getSources());
        return Response.success("list", questionBanks.getContent(), "page", page, "count", questionBanks.getTotalElements(), "total_page", questionBanks.getTotalPages()).entity();
    }

    /**
     *
     */
    @ResponseBody
    @PostMapping("/bank/")
    public ResponseEntity<Map<String, Object>> bankAdd(@RequestBody @Valid QuestionBank form, Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        this.questionBankService.addBank(form, loginUser.getSources());
        return Response.success("success", true).entity();
    }

    /**
     *
     */
    @ResponseBody
    @PutMapping("/bank/{id}/")
    public ResponseEntity<Map<String, Object>> bankUpdate(@PathVariable Long id, @RequestBody @Valid QuestionBank form, Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        this.questionBankService.updateBank(id, form, loginUser.getSources());
        return Response.success("success", true).entity();
    }

    /**
     *
     */
    @ResponseBody
    @DeleteMapping("/bank/{id}/")
    public ResponseEntity<Map<String, Object>> bankDelete(@PathVariable Long id, Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        this.questionBankService.deleteBank(id, loginUser.getSources());
        return Response.success("success", true).entity();
    }

    /**
     *
     */
    @Autowired
    QuestionBankService questionBankService;
}