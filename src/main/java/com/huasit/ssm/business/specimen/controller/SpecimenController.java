package com.huasit.ssm.business.specimen.controller;

import com.huasit.ssm.business.specimen.entity.Specimen;
import com.huasit.ssm.business.specimen.service.SpecimenService;
import com.huasit.ssm.system.locale.Response;
import com.huasit.ssm.system.security.model.AuthenticationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/specimen")
public class SpecimenController {

    /**
     *
     */
    @ResponseBody
    @GetMapping("/study/{id}/")
    public ResponseEntity<Map<String, Object>> study(@PathVariable Long id, @RequestParam(name = "interval") int interval, Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        Specimen specimen = this.specimenService.study(id, interval, loginUser.getSources());
        int studyTiming = this.specimenService.getStudyTiming(loginUser.getSources().getId(), id);
        return Response.success("specimen", specimen, "study_timing", studyTiming).entity();
    }

    /**
     *
     */
    @ResponseBody
    @GetMapping("/{id}/")
    public ResponseEntity<Map<String, Object>> get(@PathVariable Long id, Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        Specimen specimen = this.specimenService.getSpecimenById(id);
        int studyTiming = this.specimenService.getStudyTiming(loginUser.getSources().getId(), id);
        return Response.success("specimen", specimen, "study_timing", studyTiming).entity();
    }

    /**
     *
     */
    @ResponseBody
    @GetMapping("/list/")
    public ResponseEntity<Map<String, Object>> list(Specimen form, @RequestParam("page") int page, @RequestParam(name = "pageSize", defaultValue = "10") int pageSize, Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        Page<Specimen> specimens = this.specimenService.list(form, page, pageSize, loginUser.getSources());
        return Response.success("list", specimens.getContent(), "page", page, "count", specimens.getTotalElements(), "total_page", specimens.getTotalPages()).entity();
    }

    /**
     *
     */
    @ResponseBody
    @PostMapping("/")
    public ResponseEntity<Map<String, Object>> add(@RequestBody Specimen form, Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        this.specimenService.add(form, loginUser.getSources());
        return Response.success("specimen", form).entity();
    }

    /**
     *
     */
    @ResponseBody
    @PutMapping("/{id}/")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody @Valid Specimen form, Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        this.specimenService.update(id, form, loginUser.getSources());
        return Response.success("success", true).entity();
    }

    /**
     *
     */
    @ResponseBody
    @DeleteMapping("/{id}/")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id, Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        this.specimenService.delete(id, loginUser.getSources());
        return Response.success("success", true).entity();
    }

    /**
     *
     */
    @ResponseBody
    @GetMapping("/study/timing/")
    public ResponseEntity<Map<String, Object>> timingList(Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        List<Object[]> list = this.specimenService.studyTimingList(loginUser.getSources());
        return Response.success("list", list).entity();
    }

    /**
     *
     */
    @Autowired
    SpecimenService specimenService;
}
