package com.huasit.ssm.core.user.controller;

import com.huasit.ssm.business.classes.entity.Classes;
import com.huasit.ssm.business.classes.service.ClassesService;
import com.huasit.ssm.business.term.entity.Term;
import com.huasit.ssm.business.term.entity.TermRepository;
import com.huasit.ssm.core.menu.entity.Menu;
import com.huasit.ssm.core.menu.service.MenuService;
import com.huasit.ssm.core.user.entity.User;
import com.huasit.ssm.core.user.service.UserService;
import com.huasit.ssm.system.exception.SystemError;
import com.huasit.ssm.system.exception.SystemException;
import com.huasit.ssm.system.locale.Response;
import com.huasit.ssm.system.security.model.AuthenticationUser;
import com.huasit.ssm.system.util.ExcelUtil;
import com.huasit.ssm.system.util.FileUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Stream;

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
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody User form, Authentication authentication) throws Exception {
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

    @ResponseBody
    @RequestMapping("/batchImport/{userType}/")
    @PreAuthorize("hasAnyRole('super','admin')")
    public ResponseEntity<Map<String, Object>> batchImport(MultipartFile formFile, Authentication authentication, @PathVariable String userType) throws Exception {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        Workbook wb = ExcelUtil.getWorkbook(FileUtil.multipartFileToFile(formFile));
        Sheet hssfSheet = wb.getSheetAt(0);

        List<Term> terms = termRepository.findAll();
        List<Classes> classesList = classesService.findAll();
        List<User> checks = userService.findByUsernameIsNotNull();

        if (hssfSheet != null) {
            List<User> users = new ArrayList<>();
            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                Row hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow != null) {
                    //检查空值
                    if (emptyRow(hssfRow, 6)) {
                        break;
                    }
                    String usernameStr = FieldCheck(rowNum, "学号", hssfRow.getCell(0));
                    String nameStr = FieldCheck(rowNum, "姓名", hssfRow.getCell(1));
                    String sexStr = FieldCheck(rowNum, "性别", hssfRow.getCell(2));
                    String termYearStr = FieldCheck(rowNum, "学年", hssfRow.getCell(3));
                    String termNumStr = FieldCheck(rowNum, "学期", hssfRow.getCell(4));
                    String classesNameStr = FieldCheck(rowNum, "班级", hssfRow.getCell(5));

                    String username = usernameStr.split("\\.")[0];
                    //检查学号是否重复
                    if (checks.parallelStream().anyMatch(u -> u.getUsername().equals(username))) {
                        throw new SystemException(SystemError.IMPORT_FIELD_DUPLICATE, rowNum, "学号");
                    }

                    int termYear = Integer.parseInt(termYearStr.split("\\.")[0]);
                    int termNum = (termNumStr.contains("一") || termNumStr.contains("1")) ? 1 : 2;
                    Optional<Term> term = terms.stream().filter(t -> t.getYear() == termYear && t.getNum() == termNum).findFirst();
                    if (!term.isPresent()) {
                        throw new SystemException(SystemError.IMPORT_FIELD_NOT_MATCH, rowNum, "学年或学期");
                    }
                    Optional<Classes> classes = classesList.stream().filter(c -> c.getName().equals(classesNameStr)).findFirst();
                    if (!classes.isPresent()) {
                        throw new SystemException(SystemError.IMPORT_FIELD_NOT_MATCH, rowNum, "班级");
                    }
                    users.add(User.builder().username(username).name(nameStr).sex(sexStr).overTerm(term.get()).classes(classes.get()).build());
                }
            }
            this.userService.addUsers(users, loginUser.getSources(), userType);
        }
        return Response.success("success", true).entity();
    }

    public String FieldCheck(int row, String field, Cell value) {
        if (null == value || value.toString().equals("")) {
            throw new SystemException(SystemError.IMPORT_FIELD_EMPTY, row + 1, field);
        }
        return value.toString();
    }

    public boolean emptyRow(Row row, int to) {
        return Stream.iterate(0, item -> item + 1).limit(to).map(row::getCell).allMatch(cell -> cell == null || cell.toString().trim().equals(""));
    }

    @ResponseBody
    @PutMapping("/disableNormalStudent/")
    public ResponseEntity<Map<String, Object>> disableNormal(Authentication authentication) {
        this.userService.disableNormalStudent();
        return Response.success("success", true).entity();
    }

    @ResponseBody
    @PutMapping("/normalDisableStudent/")
    public ResponseEntity<Map<String, Object>> normalDisable(Authentication authentication) {
        this.userService.normalDisableStudent();
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

    @Autowired
    TermRepository termRepository;
    @Autowired
    ClassesService classesService;
}