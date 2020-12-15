package com.huasit.ssm.business.laboratory.controller;

import com.huasit.ssm.business.laboratory.entity.LaboratoryBook;
import com.huasit.ssm.business.laboratory.service.LaboratoryBookService;
import com.huasit.ssm.business.laboratory.service.LaboratoryService;
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
@RequestMapping("/laboratory/book")
public class LaboratoryBookController {

    /**
     *
     */
    @ResponseBody
    @GetMapping("/list/")
    public ResponseEntity<Map<String, Object>> list(LaboratoryBook form, @RequestParam("page") int page, @RequestParam(name = "pageSize", defaultValue = "10") int pageSize, Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        Page<LaboratoryBook> laboratoryBooks   = this.laboratoryBookService.list(form, page, pageSize, loginUser.getSources());
        return Response.success("list", laboratoryBooks.getContent(), "page", page, "count", laboratoryBooks.getTotalElements(), "total_page", laboratoryBooks.getTotalPages()).entity();
    }

    /**
     *
     */
    @ResponseBody
    @PostMapping("/")
    public ResponseEntity<Map<String, Object>> add(@RequestBody LaboratoryBook form, Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        this.laboratoryBookService.add(form, loginUser.getSources());
        return Response.success("laboratory_book", form).entity();
    }

    /**
     *
     */
    @ResponseBody
    @PutMapping("/{id}/")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody @Valid LaboratoryBook form, Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        this.laboratoryBookService.update(id, form, loginUser.getSources());
        return Response.success("success", true).entity();
    }

    /**
     *
     */
    @ResponseBody
    @DeleteMapping("/{id}/")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id, Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        this.laboratoryBookService.delete(id, loginUser.getSources());
        return Response.success("success", true).entity();
    }

    /**
     *
     */
    @Autowired
    LaboratoryService laboratoryService;

    /**
     *
     */
    @Autowired
    LaboratoryBookService laboratoryBookService;
}