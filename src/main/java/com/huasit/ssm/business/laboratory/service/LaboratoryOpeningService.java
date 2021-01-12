package com.huasit.ssm.business.laboratory.service;

import com.huasit.ssm.business.laboratory.entity.LaboratoryOpening;
import com.huasit.ssm.business.laboratory.entity.LaboratoryOpeningRepository;
import com.huasit.ssm.business.laboratory.entity.LaboratoryRepository;
import com.huasit.ssm.core.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class LaboratoryOpeningService {

    /**
     *
     */
    public List<LaboratoryOpening> list(Long laboratoryId, String date) {
        return this.laboratoryOpeningRepository.findByLaboratoryIdAndDate(laboratoryId, date + "-%");
    }

    /**
     *
     */
    public LaboratoryOpening getByLaboratoryIdAndDay(Long laboratoryId, String day) {
        return this.laboratoryOpeningRepository.findByLaboratoryIdAndDay(laboratoryId, day);
    }


    /**
     *
     */
    public void save(LaboratoryOpening form, User loginUser) {
        LaboratoryOpening l = this.getByLaboratoryIdAndDay(form.getLaboratoryId(), form.getDay());
        if(l != null) {
            l.setAllowHours(form.getAllowHours());
        }
        this.laboratoryOpeningRepository.save(form);
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
    LaboratoryRepository laboratoryRepository;

    /**
     *
     */
    @Autowired
    LaboratoryOpeningRepository laboratoryOpeningRepository;
}