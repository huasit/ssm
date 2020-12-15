package com.huasit.ssm.business.laboratory.service;

import com.huasit.ssm.business.laboratory.entity.Laboratory;
import com.huasit.ssm.business.laboratory.entity.LaboratoryRepository;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class LaboratoryService {

    /**
     *
     */
    public Laboratory getLaboratoryById(Long id) {
        Laboratory laboratory = this.laboratoryRepository.findById(id).orElse(null);
        if (laboratory == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<String> applyBookingDates = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for (int n = 0; n <= 7; n++) {
            applyBookingDates.add(sdf.format(calendar.getTime()));
            calendar.add(Calendar.DATE, 1);
        }
        laboratory.setApplyBookingDates(applyBookingDates);
        return laboratory;
    }

    /**
     *
     */
    public Page<Laboratory> list(Laboratory form, int page, int pageSize, User loginUser) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Order.asc("id")));
        return this.laboratoryRepository.findAll((Specification<Laboratory>) (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("del").as(boolean.class), false));
            query.where(predicates.toArray(new Predicate[0]));
            return query.getRestriction();
        }, pageRequest);
    }

    /**
     *
     */
    public void add(Laboratory form, User loginUser) {
        form.setCreateTime(new Date());
        form.setCreatorId(loginUser.getId());
        form.setId(null);
        form.setDel(false);
        this.laboratoryRepository.save(form);
    }

    /**
     *
     */
    public void update(Long id, Laboratory form, User loginUser) {
        Laboratory db = this.laboratoryRepository.findById(id).orElseThrow(() -> new SystemException(SystemError.FORMDATA_ERROR));
        form.setId(id);
        form.setDel(db.isDel());
        form.setCreatorId(db.getCreatorId());
        form.setCreateTime(db.getCreateTime());
        this.laboratoryRepository.save(form);
    }

    /**
     *
     */
    public void delete(Long id, User loginUser) {
        this.laboratoryRepository.updateDel(true, id);
    }

    /**
     *
     */
    @Autowired
    LaboratoryRepository laboratoryRepository;
}
