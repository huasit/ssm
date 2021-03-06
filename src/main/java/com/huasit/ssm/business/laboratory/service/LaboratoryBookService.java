package com.huasit.ssm.business.laboratory.service;

import com.huasit.ssm.business.duty.entity.Duty;
import com.huasit.ssm.business.duty.entity.DutyRepository;
import com.huasit.ssm.business.laboratory.entity.*;
import com.huasit.ssm.core.user.entity.User;
import com.huasit.ssm.system.exception.SystemError;
import com.huasit.ssm.system.exception.SystemException;
import com.huasit.ssm.system.util.DateUtil;
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
public class LaboratoryBookService {

    /**
     *
     */
    public Page<LaboratoryBook> list(LaboratoryBook form, int page, int pageSize, User loginUser) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Order.desc("id")));
        return this.laboratoryBookRepository.findAll((Specification<LaboratoryBook>) (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("del").as(boolean.class), false));
            predicates.add(cb.equal(root.get("userId").as(Long.class), loginUser.getId()));
            query.where(predicates.toArray(new Predicate[0]));
            return query.getRestriction();
        }, pageRequest);
    }

    /**
     *
     */
    public void add(LaboratoryBook form, User loginUser) {
        Laboratory laboratory = this.laboratoryRepository.findById(form.getLaboratory().getId()).orElseThrow(() -> new SystemException(SystemError.FORMDATA_ERROR));
        this.isAvailableBookTime(form.getBookDay(), new Integer[]{form.getBookHour(), form.getBookHour2()});
        this.isNotInLaboratoryAllowHours(laboratory, form.getBookDay(), new Integer[]{form.getBookHour(), form.getBookHour2()});
        this.userHasBookInSameTime(loginUser, form.getBookDay(), new Integer[]{form.getBookHour(), form.getBookHour2()}, null);
        this.isOutLaboratoryCapacityLimit(laboratory, form.getBookDay(), new Integer[]{form.getBookHour(), form.getBookHour2()}, null);
        form.setLaboratory(laboratory);
        form.setUserId(loginUser.getId());
        form.setCreateTime(new Date());
        form.setCreatorId(loginUser.getId());
        form.setId(null);
        form.setDel(false);
        this.laboratoryBookRepository.save(form);
    }

    /**
     *
     */
    public void update(Long id, LaboratoryBook form, User loginUser) {
        LaboratoryBook db = this.laboratoryBookRepository.findById(id).orElseThrow(() -> new SystemException(SystemError.FORMDATA_ERROR));
        this.isAvailableBookTime(form.getBookDay(), new Integer[]{form.getBookHour(), form.getBookHour2()});
        this.isNotInLaboratoryAllowHours(db.getLaboratory(), form.getBookDay(), new Integer[]{form.getBookHour(), form.getBookHour2()});
        this.userHasBookInSameTime(loginUser, form.getBookDay(), new Integer[]{form.getBookHour(), form.getBookHour2()}, db.getId());
        this.isOutLaboratoryCapacityLimit(db.getLaboratory(), form.getBookDay(), new Integer[]{form.getBookHour(), form.getBookHour2()}, db.getId());
        form.setId(id);
        form.setLaboratory(db.getLaboratory());
        form.setDel(db.isDel());
        form.setCreatorId(db.getCreatorId());
        form.setCreateTime(db.getCreateTime());
        this.laboratoryBookRepository.save(form);
    }

    /**
     *
     */
    public void delete(Long id, User loginUser) {
        LaboratoryBook book = this.laboratoryBookRepository.findById(id).orElseThrow(() -> new SystemException(SystemError.FORMDATA_ERROR));
        if (book.getBookDay().equals(DateUtil.getTodayDate())) {
            int thisHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            if ((book.getBookHour() != null && book.getBookHour() < thisHour) || (book.getBookHour2() != null && book.getBookHour2() < thisHour)) {
                throw new SystemException(SystemError.LABORATORY_BOOK_TIME_NOT_AVAILABLE);
            }
        } else if (book.getBookDay().before(DateUtil.getTodayDate())) {
            throw new SystemException(SystemError.LABORATORY_BOOK_TIME_NOT_AVAILABLE);
        }
        this.laboratoryBookRepository.updateDel(true, id);
    }

    /**
     *
     */
    public LaboratoryBook getUserBook(User user) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        return this.laboratoryBookRepository.findByUserAndTime(user.getId(), DateUtil.getTodayDate(), hour);
    }

    /**
     *
     */
    public void isAvailableBookTime(Date bookDay, Integer[] hours) {
        if (bookDay.before(DateUtil.getTodayDate())) {
            return;
        }
        if (bookDay.equals(DateUtil.getTodayDate())) {
            int thisHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            for (Integer hour : hours) {
                if (hour == null) {
                    continue;
                }
                if (hour < thisHour) {
                    throw new SystemException(SystemError.LABORATORY_BOOK_TIME_NOT_AVAILABLE, DateUtil.getDateStr(bookDay), hour);
                }
            }
        }
    }

    /**
     *
     */
    public void userHasBookInSameTime(User user, Date bookDay, Integer[] hours, Long oldId) {
        for (Integer hour : hours) {
            if (hour == null) {
                continue;
            }
            if (oldId == null) {
                if (this.laboratoryBookRepository.findBookCountByUser(user.getId(), bookDay, hour) > 0) {
                    throw new SystemException(SystemError.LABORATORY_BOOK_USER_HAS_BOOK_IN_SAMETIME, DateUtil.getDateStr(bookDay), hour);
                }
            } else {
                if (this.laboratoryBookRepository.findBookCountByUser(oldId, user.getId(), bookDay, hour) > 0) {
                    throw new SystemException(SystemError.LABORATORY_BOOK_USER_HAS_BOOK_IN_SAMETIME, DateUtil.getDateStr(bookDay), hour);
                }
            }
        }
    }

    /**
     *
     */
    public void isNotInLaboratoryAllowHours(Laboratory laboratory, Date day, Integer[] hours) {
        LaboratoryOpening l = this.laboratoryOpeningService.getByLaboratoryIdAndDay(laboratory.getId(), new SimpleDateFormat("yyyy-dd-mm").format(day));
        String allowHours = "8,9,10,11,12,13,14,15,16,17,18,19";
        if (l != null) {
            allowHours = l.getAllowHours();
        }
        for (Integer hour : hours) {
            if (hour == null) {
                continue;
            }
            if (!("," + allowHours + ",").contains("," + hour + ",")) {
                throw new SystemException(SystemError.LABORATORY_BOOK_NOT_IN_ALLOW_HOURS);
            }
        }
    }

    /**
     *
     */
    public void isOutLaboratoryCapacityLimit(Laboratory laboratory, Date bookDay, Integer[] hours, Long oldId) {
        for (Integer hour : hours) {
            if (hour == null) {
                continue;
            }
            if (oldId == null) {
                if (this.laboratoryBookRepository.findBookCountByLaboratory(laboratory.getId(), bookDay, hour) >= laboratory.getReserve() + laboratory.getCapacity()) {
                    throw new SystemException(SystemError.LABORATORY_BOOK_CAPACITY_LIMIT, DateUtil.getDateStr(bookDay), hour);
                }
            } else {
                if (this.laboratoryBookRepository.findBookCountByLaboratory(oldId, laboratory.getId(), bookDay, hour) >= laboratory.getReserve() + laboratory.getCapacity()) {
                    throw new SystemException(SystemError.LABORATORY_BOOK_CAPACITY_LIMIT, DateUtil.getDateStr(bookDay), hour);
                }
            }
        }
    }

    /**
     *
     */
    public void updateBookTeacher(LaboratoryBook book) {
        Duty duty = this.dutyRepository.findByDate(book.getLaboratory().getId(), book.getBookDay());
        if (duty == null) {
            return;
        }
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (book.getBookHour() != null && hour == book.getBookHour() && book.getTeacherId() == null) {
            User teacher;
            if (book.getBookHour() < 12) {
                teacher = duty.getAmUser();
            } else if (book.getBookHour() < 18) {
                teacher = duty.getPmUser();
            } else {
                teacher = duty.getNgUser();
            }
            if (teacher != null) {
                book.setTeacherId(teacher.getId());
                this.laboratoryBookRepository.updateTeacherId(teacher.getId(), book.getId());
            }
        }
        if (book.getBookHour2() != null && hour == book.getBookHour2() && book.getTeacherId2() == null) {
            User teacher;
            if (book.getBookHour2() < 12) {
                teacher = duty.getAmUser();
            } else if (book.getBookHour2() < 18) {
                teacher = duty.getPmUser();
            } else {
                teacher = duty.getNgUser();
            }
            if (teacher != null) {
                book.setTeacherId2(teacher.getId());
                this.laboratoryBookRepository.updateTeacherId2(teacher.getId(), book.getId());
            }
        }
    }

    /**
     *
     */
    @Autowired
    DutyRepository dutyRepository;

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
    LaboratoryBookRepository laboratoryBookRepository;

    /**
     *
     */
    @Autowired
    LaboratoryOpeningService laboratoryOpeningService;
}