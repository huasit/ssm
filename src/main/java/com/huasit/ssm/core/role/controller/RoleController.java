package com.huasit.ssm.core.role.controller;

import com.huasit.ssm.core.role.entity.Role;
import com.huasit.ssm.core.role.entity.RoleMenu;
import com.huasit.ssm.core.role.entity.RoleUser;
import com.huasit.ssm.core.role.service.RoleService;
import com.huasit.ssm.system.locale.Response;
import com.huasit.ssm.system.security.model.AuthenticationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Controller
@RequestMapping("/role")
public class RoleController {

    /**
     *
     */
    @ResponseBody
    @GetMapping("/{id}/")
    public ResponseEntity<Map<String, Object>> get(@PathVariable Long id, Authentication authentication) {
        Role role = this.roleService.getRoleById(id);
        return Response.success("role", role).entity();
    }

    /**
     *
     */
    @ResponseBody
    @GetMapping("/list/")
    @PreAuthorize("hasAnyRole('super','admin')")
    public ResponseEntity<Map<String, Object>> list(Role form, @RequestParam("page") int page, @RequestParam(name = "pageSize", defaultValue = "10") int pageSize, Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        Page<Role> users = this.roleService.list(form, page, pageSize, loginUser.getSources());
        return Response.success("list", users.getContent(), "page", page, "count", users.getTotalElements(), "total_page", users.getTotalPages()).entity();
    }

    /**
     *
     */
    @ResponseBody
    @PostMapping("/")
    @PreAuthorize("hasAnyRole('super','admin')")
    public ResponseEntity<Map<String, Object>> add(@RequestBody @Valid Role form, Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        this.roleService.add(form, loginUser.getSources());
        return Response.success("success", true).entity();
    }

    /**
     *
     */
    @ResponseBody
    @PutMapping("/{id}/")
    @PreAuthorize("hasAnyRole('super','admin')")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody @Valid Role form, Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        this.roleService.update(id, form, loginUser.getSources());
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
        this.roleService.delete(id, loginUser.getSources());
        return Response.success("success", true).entity();
    }

    /**
     *
     */
    @ResponseBody
    @GetMapping("/{roleId}/user/list/")
    @PreAuthorize("hasAnyRole('super','admin')")
    public ResponseEntity<Map<String, Object>> userList(@PathVariable Long roleId, @RequestParam("page") int page, @RequestParam(name = "pageSize", defaultValue = "10") int pageSize, Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        Page<RoleUser> roleUsers = this.roleService.userList(roleId, page, pageSize, loginUser.getSources());
        return Response.success("list", roleUsers.getContent(), "page", page, "count", roleUsers.getTotalElements(), "total_page", roleUsers.getTotalPages()).entity();
    }

    /**
     *
     */
    @ResponseBody
    @PutMapping("/{roleId}/user/")
    @PreAuthorize("hasAnyRole('super','admin')")
    public ResponseEntity<Map<String, Object>> userUpdate(@PathVariable Long roleId, @RequestBody Role role, Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        this.roleService.userUpdate(roleId, role.getUsers(), loginUser.getSources());
        return Response.success("success", true).entity();
    }

    /**
     *
     */
    @ResponseBody
    @GetMapping("/{roleId}/menu/list/")
    @PreAuthorize("hasAnyRole('super','admin')")
    public ResponseEntity<Map<String, Object>> menuList(@PathVariable Long roleId, @RequestParam("page") int page, @RequestParam(name = "pageSize", defaultValue = "10") int pageSize, Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        Page<RoleMenu> roleMenus = this.roleService.menuList(roleId, page, pageSize, loginUser.getSources());
        return Response.success("list", roleMenus.getContent(), "page", page, "count", roleMenus.getTotalElements(), "total_page", roleMenus.getTotalPages()).entity();
    }

    /**
     *
     */
    @ResponseBody
    @PutMapping("/{roleId}/menu/")
    @PreAuthorize("hasAnyRole('super','admin')")
    public ResponseEntity<Map<String, Object>> menuUpdate(@PathVariable Long roleId, @RequestBody Role role, Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        this.roleService.menuUpdate(roleId, role.getMenus(), loginUser.getSources());
        return Response.success("success", true).entity();
    }

    /**
     *
     */
    @Autowired
    RoleService roleService;
}