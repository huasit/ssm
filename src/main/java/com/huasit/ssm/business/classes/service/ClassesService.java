package com.huasit.ssm.business.classes.service;

import com.huasit.ssm.business.classes.entity.Classes;
import com.huasit.ssm.business.classes.entity.ClassesRepository;
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
public class ClassesService {

    public List<Classes> findAll() {
        return this.classesRepository.findAll();
    }

    public Classes getById(Long id) {
        return this.classesRepository.findById(id).orElse(null);
    }

    /**
     *
     */
    public Page<Classes> list(int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Order.asc("id")));
        return this.classesRepository.findAll((Specification<Classes>) (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("del").as(boolean.class), false));
            query.where(predicates.toArray(new Predicate[0]));
            return query.getRestriction();
        }, pageRequest);
    }

    /**
     *
     */
    public void add(Classes form, User loginUser) {
        form.setCreateTime(new Date());
        form.setCreatorId(loginUser.getId());
        form.setId(null);
        form.setDel(false);
        this.classesRepository.save(form);
    }

    /**
     *
     */
    public void update(Long id, Classes form) {
        Classes db = this.classesRepository.findById(id).orElseThrow(() -> new SystemException(SystemError.FORMDATA_ERROR));
        form.setId(id);
        form.setDel(db.isDel());
        form.setCreatorId(db.getCreatorId());
        form.setCreateTime(db.getCreateTime());
        this.classesRepository.save(form);
    }

    /**
     *
     */
    public void delete(Long id) {
        this.classesRepository.updateDel(true, id);
    }

    /**
     *
     */
    @Autowired
    ClassesRepository classesRepository;
}
