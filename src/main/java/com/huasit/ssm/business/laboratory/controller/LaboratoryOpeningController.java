package com.huasit.ssm.business.laboratory.controller;

import com.huasit.ssm.business.laboratory.entity.LaboratoryOpening;
import com.huasit.ssm.business.laboratory.service.LaboratoryOpeningService;
import com.huasit.ssm.business.laboratory.service.LaboratoryService;
import com.huasit.ssm.system.locale.Response;
import com.huasit.ssm.system.security.model.AuthenticationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/laboratory/opening")
public class LaboratoryOpeningController {

    /**
     *
     */
    @ResponseBody
    @GetMapping("/list/")
    public ResponseEntity<Map<String, Object>> list(Long laboratoryId, String date, Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        List<LaboratoryOpening> laboratoryOpenings = this.laboratoryOpeningService.list(laboratoryId, date);
        return Response.success("list", laboratoryOpenings).entity();
    }

    /**
     *
     */
    @ResponseBody
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> get(Long laboratoryId, String day, Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        LaboratoryOpening laboratoryOpening = this.laboratoryOpeningService.getByLaboratoryIdAndDay(laboratoryId, day);
        if(laboratoryOpening == null) {
            return Response.success("empty", true).entity();
        }
        return Response.success("laboratory_opening", laboratoryOpening).entity();
    }

    /**
     *
     */
    @ResponseBody
    @PostMapping("/")
    public ResponseEntity<Map<String, Object>> add(@RequestBody LaboratoryOpening form, Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        this.laboratoryOpeningService.save(form, loginUser.getSources());
        return Response.success("laboratory_opening", form).entity();
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
    LaboratoryOpeningService laboratoryOpeningService;
}