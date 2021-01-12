package com.huasit.ssm.business.duty.service;

import com.huasit.ssm.business.duty.entity.Duty;
import com.huasit.ssm.business.duty.entity.DutyRepository;
import com.huasit.ssm.core.user.entity.User;
import com.huasit.ssm.core.user.entity.UserRepository;
import com.huasit.ssm.system.exception.SystemError;
import com.huasit.ssm.system.exception.SystemException;
import com.huasit.ssm.system.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class DutyService {

    /**
     *
     */
    public User getDutyTeacher(Long laboratoryId, int hour) {
        Date today = DateUtil.getTodayDate();
        Duty duty = this.dutyRepository.findByDate(laboratoryId, today);
        if (duty == null) {
            return null;
        }
        if (hour < 12) {
            return duty.getAmUser();
        } else if (hour < 18) {
            return duty.getPmUser();
        } else {
            return duty.getNgUser();
        }
    }

    /**
     *
     */
    public List<Duty> list(Long laboratoryId, String date, User loginUser) throws Exception {
        Date from = new SimpleDateFormat("yyyy-MM").parse(date);
        Calendar toC = Calendar.getInstance();
        toC.setTime(from);
        toC.set(Calendar.DAY_OF_MONTH, toC.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date to = toC.getTime();
        return this.dutyRepository.findByDate(laboratoryId, from, to);
    }

    /**
     *
     */
    public Duty getDutyByDate(Long laboratoryId, String dateStr) throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        return this.dutyRepository.findByDate(laboratoryId, date);
    }

    /**
     *
     */
    public Duty getDutyById(Long id) {
        return this.dutyRepository.findById(id).orElse(null);
    }

    /**
     *
     */
    public void add(Duty form, User loginUser) {
        if (form.getAmUser() != null && form.getAmUser().getId() != null) {
            form.setAmUser(this.userRepository.findById(form.getAmUser().getId()).orElseThrow(() -> new SystemException(SystemError.FORMDATA_ERROR)));
        } else {
            form.setAmUser(null);
        }
        if (form.getPmUser() != null && form.getPmUser().getId() != null) {
            form.setPmUser(this.userRepository.findById(form.getPmUser().getId()).orElseThrow(() -> new SystemException(SystemError.FORMDATA_ERROR)));
        } else {
            form.setPmUser(null);
        }
        if (form.getNgUser() != null && form.getNgUser().getId() != null) {
            form.setNgUser(this.userRepository.findById(form.getNgUser().getId()).orElseThrow(() -> new SystemException(SystemError.FORMDATA_ERROR)));
        } else {
            form.setNgUser(null);
        }
        this.dutyRepository.save(form);
    }

    /**
     *
     */
    public void update(Long id, Duty form, User loginUser) {
        Duty db = this.dutyRepository.findById(id).orElseThrow(() -> new SystemException(SystemError.FORMDATA_ERROR));
        form.setId(id);
        if (form.getAmUser() != null && form.getAmUser().getId() != null) {
            form.setAmUser(this.userRepository.findById(form.getAmUser().getId()).orElseThrow(() -> new SystemException(SystemError.FORMDATA_ERROR)));
        } else {
            form.setAmUser(null);
        }
        if (form.getPmUser() != null && form.getPmUser().getId() != null) {
            form.setPmUser(this.userRepository.findById(form.getPmUser().getId()).orElseThrow(() -> new SystemException(SystemError.FORMDATA_ERROR)));
        } else {
            form.setPmUser(null);
        }
        if (form.getNgUser() != null && form.getNgUser().getId() != null) {
            form.setNgUser(this.userRepository.findById(form.getNgUser().getId()).orElseThrow(() -> new SystemException(SystemError.FORMDATA_ERROR)));
        } else {
            form.setNgUser(null);
        }
        this.dutyRepository.save(form);
    }

    /**
     *
     */
    public void delete(Long id, User loginUser) {
        this.dutyRepository.delete(id);
    }

    /**
     *
     */
    @Autowired
    UserRepository userRepository;

    /**
     *
     */
    @Autowired
    DutyRepository dutyRepository;
}