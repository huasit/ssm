package com.huasit.ssm.business.classes.controller;

import com.huasit.ssm.business.classes.entity.Classes;
import com.huasit.ssm.business.classes.service.ClassesService;
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
@RequestMapping("/classes")
public class ClassesController {

    /**
     *
     */
    @ResponseBody
    @GetMapping("/{id}/")
    public ResponseEntity<Map<String, Object>> get(@PathVariable Long id, Authentication authentication) {
        Classes classes = this.classesService.getById(id);
        return Response.success("classes", classes).entity();
    }

    /**
     *
     */
    @ResponseBody
    @GetMapping("/list/")
    public ResponseEntity<Map<String, Object>> list(@RequestParam("page") int page, @RequestParam(name = "pageSize", defaultValue = "10") int pageSize, Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        Page<Classes> classes = this.classesService.list(page, pageSize);
        return Response.success("list", classes.getContent(), "page", page, "count", classes.getTotalElements(), "total_page", classes.getTotalPages()).entity();
    }

    /**
     *
     */
    @ResponseBody
    @PostMapping("/")
    public ResponseEntity<Map<String, Object>> add(@RequestBody @Valid Classes form, Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        this.classesService.add(form, loginUser.getSources());
        return Response.success("success", true).entity();
    }

    /**
     *
     */
    @ResponseBody
    @PutMapping("/{id}/")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody @Valid Classes form, Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        this.classesService.update(id, form);
        return Response.success("success", true).entity();
    }

    /**
     *
     */
    @ResponseBody
    @DeleteMapping("/{id}/")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id, Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        this.classesService.delete(id);
        return Response.success("success", true).entity();
    }

    /**
     *
     */
    @Autowired
    ClassesService classesService;
}
