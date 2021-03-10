package com.huasit.ssm.core.user.service;

import com.huasit.ssm.business.classes.entity.Classes;
import com.huasit.ssm.business.classes.entity.ClassesRepository;
import com.huasit.ssm.business.duty.entity.DutyRepository;
import com.huasit.ssm.business.term.entity.Term;
import com.huasit.ssm.business.term.service.TermService;
import com.huasit.ssm.core.permission.entity.Permission;
import com.huasit.ssm.core.permission.entity.PermissionRepository;
import com.huasit.ssm.core.role.entity.Role;
import com.huasit.ssm.core.role.entity.RoleRepository;
import com.huasit.ssm.core.user.entity.User;
import com.huasit.ssm.core.user.entity.UserRepository;
import com.huasit.ssm.core.user.entity.UserToken;
import com.huasit.ssm.core.user.entity.UserTokenRepository;
import com.huasit.ssm.system.exception.SystemError;
import com.huasit.ssm.system.exception.SystemException;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserService {

    /**
     *
     */
    public User getUserById(Long id) {
        User user = this.userRepository.findById(id).orElse(null);
        return user;
    }

    /**
     *
     */
    public Page<User> list(User form, int page, int pageSize, User loginUser) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Order.desc("id")));
        return this.userRepository.findAll((Specification<User>) (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (form.getType() == User.UserType.TEACHER) {
                predicates.add(cb.equal(root.get("login").as(boolean.class), form.isLogin()));
            }
            if (form.getState() != null) {
                predicates.add(cb.equal(root.get("state").as(User.UserState.class), form.getState()));
            }
            if (form.getUsername() != null && !form.getUsername().equals("")) {
                predicates.add(cb.like(root.get("username"), "%" + form.getUsername() + "%"));
            }
            if (form.getName() != null && !form.getName().equals("")) {
                predicates.add(cb.like(root.get("name"), "%" + form.getName() + "%"));
            }
            if (form.getClasses() != null && form.getClasses().getId() != null) {
                predicates.add(cb.equal(root.get("classes"), form.getClasses()));
            }
            if (form.getClasses() != null && form.getClasses().getTeacher().getId() != null) {
                predicates.add(cb.equal(root.get("classes").get("teacher"), form.getClasses().getTeacher()));
            }
            predicates.add(cb.equal(root.get("type").as(User.UserType.class), form.getType()));
            query.where(predicates.toArray(new Predicate[0]));
            return query.getRestriction();
        }, pageRequest);
    }

    /**
     *
     */
    public void add(User form, User loginUser) {
        form.setId(null);
        form.setCreateTime(new Date());
        form.setCreatorId(loginUser.getId());
        form.setState(User.UserState.NORMAL);
        if (form.getType() == User.UserType.STUDENT) {
            form.setLogin(true);
            Term term = this.termService.getTerm(form.getOverTerm().getYear(), form.getOverTerm().getNum());
            if (term == null) {
                throw new SystemException(SystemError.TERM_ERROR);
            }
            form.setOverTerm(term);
        } else {
            form.setOverTerm(null);
        }
        User usernameCheck = this.userRepository.findByUsername(form.getUsername());
        if (usernameCheck != null) {
            throw new SystemException(SystemError.USERNAME_EXISTS);
        }
        if (StringUtil.isNullOrEmpty(form.getPassword())) {
            form.setPassword("");
        } else {
            form.setPassword(new BCryptPasswordEncoder().encode(form.getPassword()));
        }
        this.userRepository.save(form);
    }

    public String addUsers(List<User> users, User loginUser, String type) {
        Date date = new Date();
        String password = new BCryptPasswordEncoder().encode("123");
        users.forEach(user -> {
            user.setId(null);
            user.setCreateTime(date);
            user.setCreatorId(loginUser.getId());
            user.setState(User.UserState.NORMAL);
            if ("student".equals(type)) {
                user.setType(User.UserType.STUDENT);
                user.setLogin(true);
                Term term = this.termService.getTerm(user.getOverTerm().getYear(), user.getOverTerm().getNum());
                if (term == null) {
                    throw new SystemException(SystemError.TERM_ERROR);
                }
                user.setOverTerm(term);
            }
            user.setPassword(password);
        });
        this.userRepository.saveAll(users);
        return "";
    }

    /**
     *
     */
    public void update(Long id, User form, User loginUser) throws Exception {
        User db = this.userRepository.findById(id).orElseThrow(() -> new SystemException(SystemError.FORMDATA_ERROR));
        form.setId(id);
        form.setType(db.getType());
        if (form.getType() == User.UserType.STUDENT) {
            form.setLogin(true);
            Term term = this.termService.getTerm(form.getOverTerm().getYear(), form.getOverTerm().getNum());
            if (term == null) {
                throw new SystemException(SystemError.TERM_ERROR);
            }
            form.setOverTerm(term);
        } else {
            form.setOverTerm(null);
        }
        User usernameCheck = this.userRepository.findByUsername(form.getUsername());
        if (usernameCheck != null && !usernameCheck.getId().equals(form.getId())) {
            throw new SystemException(SystemError.USERNAME_EXISTS);
        }
        if (StringUtil.isNullOrEmpty(form.getPassword())) {
            form.setPassword(db.getPassword());
        } else {
            form.setPassword(new BCryptPasswordEncoder().encode(form.getPassword()));
        }
        form.setCreatorId(db.getCreatorId());
        form.setCreateTime(db.getCreateTime());
        //教师禁用/删除检查
        if (form.getType() == User.UserType.TEACHER && (form.getState() == User.UserState.DISABLE || form.getState() == User.UserState.DELETE)) {
            teacherDisableCheck(form);
        }
        this.userRepository.save(form);
    }

    private void teacherDisableCheck(User user) throws Exception {
        String stateName = user.getState() == User.UserState.DISABLE ? "禁用" : "删除";
        //检查是否设定为班级的老师
        Classes classes = classesRepository.findByTeacher(user);
        if (classes != null) {
            throw new Exception(user.getName() + "已分配到" + classes.getName() + ",请先重新分配后再" + stateName + ".");
        }
        //检查是否有大于等于今天的值班安排
        if (dutyRepository.teacherUnCompleteDuty(user.getId()) > 0) {
            throw new Exception(user.getName() + "有未完成的值班安排,请重新安排值班或值班日过后再" + stateName + ".");
        }
    }

    /**
     *
     */
    public void updatePassword(User form, User loginUser) {
        this.userRepository.updatePassword(loginUser.getId(), new BCryptPasswordEncoder().encode(form.getPassword()));
    }

    /**
     *
     */
    public void delete(Long id, User loginUser) {
        this.userRepository.updateState(User.UserState.DELETE, id);
    }

    /**
     *
     */
    public User loadLoginUserByToken(String token) {
        UserToken userToken = this.userTokenRepository.findByToken(token);
        if (userToken == null || userToken.getExpireDate().before(new Date()) || !userToken.getUser().isLogin()) {
            return null;
        }
        User user = userToken.getUser();
        List<Role> roles = this.roleRepository.findRolesByUserId(user.getId());
        user.setRoles(roles);
        List<Permission> permissions = this.permissionRepository.findPermissionsByUserId(user.getId());
        user.setPermissions(permissions);
        return user;
    }

    /**
     *
     */
    public User loadLoginUserByUsername(String username) {
        User user = this.userRepository.findByUsername(username);
        if (user == null || !user.isLogin()) {
            return null;
        }
        List<Role> roles = this.roleRepository.findRolesByUserId(user.getId());
        user.setRoles(roles);
        List<Permission> permissions = this.permissionRepository.findPermissionsByUserId(user.getId());
        user.setPermissions(permissions);
        return user;
    }

    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    public List<User> findByUsernameIsNotNull() {
        return this.userRepository.findByUsernameIsNotNull();
    }

    /**
     *
     */
    @Value("${authorization.expiration}")
    private long tokenExpiration;

    /**
     *
     */
    public UserToken createLoginUserToken(String username, String ip, String client) {
        Date now = new Date();
        User user = this.userRepository.findByUsername(username);
        UserToken userToken = new UserToken();
        userToken.setEnable(true);
        userToken.setUser(user);
        userToken.setToken(UUID.randomUUID().toString());
        userToken.setExpireDate(new Date(now.getTime() + tokenExpiration * 1000));
        userToken.setIp(ip);
        userToken.setClient(client);
        userToken.setCreateTime(now);
        this.userTokenRepository.save(userToken);
        return userToken;
    }

    public void disableNormalStudent() {
        this.userRepository.disableNormalStudent();
    }

    public void normalDisableStudent() {
        this.userRepository.normalDisableStudent();
    }

    /**
     *
     */
    @Autowired
    TermService termService;

    /**
     *
     */
    @Autowired
    UserRepository userRepository;

    /**
     *
     */
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    ClassesRepository classesRepository;
    @Autowired
    DutyRepository dutyRepository;
    /**
     *
     */
    @Autowired
    UserTokenRepository userTokenRepository;

    /**
     *
     */
    @Autowired
    PermissionRepository permissionRepository;
}