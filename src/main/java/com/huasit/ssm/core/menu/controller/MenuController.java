package com.huasit.ssm.core.menu.controller;

import com.huasit.ssm.core.menu.entity.Menu;
import com.huasit.ssm.core.menu.service.MenuService;
import com.huasit.ssm.system.locale.Response;
import com.huasit.ssm.system.security.model.AuthenticationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/menu")
public class MenuController {

    /**
     *
     */
    @ResponseBody
    @GetMapping("/list/")
    @PreAuthorize("hasAnyRole('super','admin')")
    public ResponseEntity<Map<String, Object>> list(Menu form, @RequestParam("page") int page, @RequestParam(name = "pageSize", defaultValue = "10") int pageSize, Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        Page<Menu> menus = this.menuService.list(form, page, pageSize, loginUser.getSources());
        return Response.success("list", menus.getContent(), "page", page, "count", menus.getTotalElements(), "total_page", menus.getTotalPages()).entity();
    }

    /**
     *
     */
    @Autowired
    MenuService menuService;
}
