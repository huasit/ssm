package com.huasit.ssm.business.specimen.controller;

import com.huasit.ssm.business.specimen.entity.Specimen;
import com.huasit.ssm.business.specimen.service.SpecimenService;
import com.huasit.ssm.core.user.entity.User;
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
@RequestMapping("/specimen")
public class SpecimenController {

    /**
     *
     */
    @ResponseBody
    @GetMapping("/study/{id}/")
    public ResponseEntity<Map<String, Object>> study(@PathVariable Long id, @RequestParam(name = "interval") int interval, Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        Specimen specimen;
        if (loginUser.getSources().getType() == User.UserType.GUEST) {
            specimen = this.specimenService.getSpecimenById(id);
        } else {
            specimen = this.specimenService.study(id, interval, loginUser.getSources());
        }
        return Response.success("specimen", specimen).entity();
    }

    /**
     *
     */
    @ResponseBody
    @GetMapping("/{id}/")
    public ResponseEntity<Map<String, Object>> get(@PathVariable Long id, Authentication authentication) {
        Specimen specimen = this.specimenService.getSpecimenById(id);
        return Response.success("specimen", specimen).entity();
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
    @Autowired
    SpecimenService specimenService;
}