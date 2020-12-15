package com.huasit.ssm.business.specimen.service;

import com.huasit.ssm.business.specimen.entity.SpecimenBank;
import com.huasit.ssm.business.specimen.entity.SpecimenBankRepository;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SpecimenBankService {

    /**
     *
     */
    public SpecimenBank getSpecimenBankById(Long id) {
        return this.specimenBankRepository.findById(id).orElse(null);
    }

    /**
     *
     */
    public Page<SpecimenBank> list(SpecimenBank form, int page, int pageSize, User loginUser) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Order.asc("id")));
        return this.specimenBankRepository.findAll((Specification<SpecimenBank>) (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("del").as(boolean.class), false));
            query.where(predicates.toArray(new Predicate[0]));
            return query.getRestriction();
        }, pageRequest);
    }

    /**
     *
     */
    public void add(SpecimenBank form, User loginUser) {
        form.setCreateTime(new Date());
        form.setCreatorId(loginUser.getId());
        form.setId(null);
        form.setDel(false);
        this.specimenBankRepository.save(form);
    }

    /**
     *
     */
    public void update(Long id, SpecimenBank form, User loginUser) {
        SpecimenBank db = this.specimenBankRepository.findById(id).orElseThrow(() -> new SystemException(SystemError.FORMDATA_ERROR));
        form.setId(id);
        form.setDel(db.isDel());
        form.setCreatorId(db.getCreatorId());
        form.setCreateTime(db.getCreateTime());
        this.specimenBankRepository.save(form);
    }

    /**
     *
     */
    public void delete(Long id, User loginUser) {
        this.specimenBankRepository.updateDel(true, id);
    }

    /**
     *
     */
    @Autowired
    SpecimenBankRepository specimenBankRepository;
}
