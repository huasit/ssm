package com.huasit.ssm.business.duty.controller;

import com.huasit.ssm.business.duty.entity.Duty;
import com.huasit.ssm.business.duty.service.DutyService;
import com.huasit.ssm.system.locale.Response;
import com.huasit.ssm.system.security.model.AuthenticationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/duty")
public class DutyController {

    /**
     *
     */
    @ResponseBody
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> get(Long laboratoryId, String date, Authentication authentication) throws Exception {
        Duty duty = this.dutyService.getDutyByDate(laboratoryId, date);
        if(duty == null) {
            return Response.success("empty", true).entity();
        }
        return Response.success("duty", duty).entity();
    }

    /**
     *
     */
    @ResponseBody
    @GetMapping("/list/")
    public ResponseEntity<Map<String, Object>> list(Long laboratoryId, String date, Authentication authentication) throws Exception {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        List<Duty> list = this.dutyService.list(laboratoryId, date, loginUser.getSources());
        return Response.success("list",list).entity();
    }

    /**
     *
     */
    @ResponseBody
    @PostMapping("/")
    public ResponseEntity<Map<String, Object>> add(@RequestBody Duty form, Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        this.dutyService.add(form, loginUser.getSources());
        return Response.success("laboratory_book", form).entity();
    }

    /**
     *
     */
    @ResponseBody
    @PutMapping("/{id}/")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody @Valid Duty form, Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        this.dutyService.update(id, form, loginUser.getSources());
        return Response.success("success", true).entity();
    }

    /**
     *
     */
    @ResponseBody
    @DeleteMapping("/{id}/")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id, Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        this.dutyService.delete(id, loginUser.getSources());
        return Response.success("success", true).entity();
    }

    /**
     *
     */
    @Autowired
    DutyService dutyService;
}