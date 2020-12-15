package com.huasit.ssm.core.role.service;

import com.huasit.ssm.core.role.entity.*;
import com.huasit.ssm.core.user.entity.User;
import com.huasit.ssm.system.exception.SystemError;
import com.huasit.ssm.system.exception.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class RoleService {

    /**
     *
     */
    public Role getRoleById(Long id) {
        return this.roleRepository.findById(id).orElse(null);
    }

    /**
     *
     */
    public Page<Role> list(Role form, int page, int pageSize, User loginUser) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Order.asc("id")));
        return this.roleRepository.findAll((Specification<Role>) (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("del").as(boolean.class), false));
            query.where(predicates.toArray(new Predicate[0]));
            return query.getRestriction();
        }, pageRequest);
    }

    /**
     *
     */
    public void add(Role form, User loginUser) {
        form.setCreateTime(new Date());
        form.setCreatorId(loginUser.getId());
        form.setId(null);
        form.setDel(false);
        Role signCheck = this.roleRepository.findBySign(form.getSign());
        if (signCheck != null) {
            throw new SystemException(SystemError.ROLE_SIGN_EXISTS);
        }
        this.roleRepository.save(form);
    }

    /**
     *
     */
    public void update(Long id, Role form, User loginUser) {
        Role db = this.roleRepository.findById(id).orElseThrow(() -> new SystemException(SystemError.FORMDATA_ERROR));
        form.setId(id);
        form.setCreatorId(db.getCreatorId());
        form.setCreateTime(db.getCreateTime());
        Role signCheck = this.roleRepository.findBySign(form.getSign());
        if (signCheck != null && !signCheck.getId().equals(form.getId())) {
            throw new SystemException(SystemError.ROLE_SIGN_EXISTS);
        }
        this.roleRepository.save(form);
    }

    /**
     *
     */
    public void delete(Long id, User loginUser) {
        this.roleRepository.updateDel(true, id);
    }

    /**
     *
     */
    public Page<RoleUser> userList(Long roleId, int page, int pageSize, User loginUser) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Order.asc("id")));
        return this.roleUserRepository.findAll((Specification<RoleUser>) (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("roleId").as(Long.class), roleId));
            query.where(predicates.toArray(new Predicate[0]));
            return query.getRestriction();
        }, pageRequest);
    }

    /**
     *
     */
    public void userUpdate(Long roleId, List<RoleUser> roleUsers, User loginUser) {
        this.roleUserRepository.deleteByRoleId(roleId);
        if(roleUsers != null) {
            Date now = new Date();
            for(RoleUser roleUser : roleUsers) {
                roleUser.setRoleId(roleId);
                roleUser.setCreateTime(now);
                roleUser.setCreatorId(loginUser.getId());
            }
            this.roleUserRepository.saveAll(roleUsers);
        }
    }


    /**
     *
     */
    public Page<RoleMenu> menuList(Long roleId, int page, int pageSize, User loginUser) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Order.asc("id")));
        return this.roleMenuRepository.findAll((Specification<RoleMenu>) (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("roleId").as(Long.class), roleId));
            query.where(predicates.toArray(new Predicate[0]));
            return query.getRestriction();
        }, pageRequest);
    }

    /**
     *
     */
    public void menuUpdate(Long roleId, List<RoleMenu> roleMenus, User loginUser) {
        this.roleMenuRepository.deleteByRoleId(roleId);
        if(roleMenus != null) {
            Date now = new Date();
            for(RoleMenu roleUser : roleMenus) {
                roleUser.setRoleId(roleId);
                roleUser.setCreateTime(now);
                roleUser.setCreatorId(loginUser.getId());
            }
            this.roleMenuRepository.saveAll(roleMenus);
        }
    }

    /**
     *
     */
    @Autowired
    RoleRepository roleRepository;

    /**
     *
     */
    @Autowired
    RoleUserRepository roleUserRepository;

    /**
     *
     */
    @Autowired
    RoleMenuRepository roleMenuRepository;
}
