package com.huasit.ssm.core.user.controller;

import com.huasit.ssm.core.menu.entity.Menu;
import com.huasit.ssm.core.menu.service.MenuService;
import com.huasit.ssm.core.user.entity.User;
import com.huasit.ssm.core.user.service.UserService;
import com.huasit.ssm.system.locale.Response;
import com.huasit.ssm.system.security.model.AuthenticationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    /**
     *
     */
    @ResponseBody
    @GetMapping("/self/")
    public ResponseEntity<Map<String, Object>> self(Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        List<Menu> menus = this.menuService.getUserMenuTree(loginUser.getSources().getId());
        return Response.success("user", loginUser.getSources(), "menus", menus).entity();
    }

    /**
     *
     */
    @ResponseBody
    @GetMapping("/{id}/")
    public ResponseEntity<Map<String, Object>> get(@PathVariable Long id, Authentication authentication) {
        User user = this.userService.getUserById(id);
        return Response.success("user", user).entity();
    }

    /**
     *
     */
    @ResponseBody
    @GetMapping("/list/")
    @PreAuthorize("hasAnyRole('super','admin')")
    public ResponseEntity<Map<String, Object>> list(User form, @RequestParam("page") int page, @RequestParam(name = "pageSize", defaultValue = "10") int pageSize, Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        Page<User> users = this.userService.list(form, page, pageSize, loginUser.getSources());
        return Response.success("list", users.getContent(), "page", page, "count", users.getTotalElements(), "total_page", users.getTotalPages()).entity();
    }

    /**
     *
     */
    @ResponseBody
    @PostMapping("/")
    @PreAuthorize("hasAnyRole('super','admin')")
    public ResponseEntity<Map<String, Object>> add(@RequestBody User form, Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        this.userService.add(form, loginUser.getSources());
        return Response.success("success", true).entity();
    }

    /**
     *
     */
    @ResponseBody
    @PutMapping("/{id}/")
    @PreAuthorize("hasAnyRole('super','admin')")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody User form, Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        this.userService.update(id, form, loginUser.getSources());
        return Response.success("success", true).entity();
    }

    /**
     *
     */
    @ResponseBody
    @DeleteMapping("/{id}/")
    @PreAuthorize("hasAnyRole('super','admin')")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id, Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        this.userService.delete(id, loginUser.getSources());
        return Response.success("success", true).entity();
    }

    /**
     *
     */
    @ResponseBody
    @PutMapping("/password/")
    public ResponseEntity<Map<String, Object>> updatePassword(@RequestBody User form, Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        this.userService.updatePassword(form, loginUser.getSources());
        return Response.success("success", true).entity();
    }

    /**
     *
     */
    @Autowired
    UserService userService;

    /**
     *
     */
    @Autowired
    MenuService menuService;
}