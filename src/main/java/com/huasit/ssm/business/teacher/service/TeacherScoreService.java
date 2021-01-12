package com.huasit.ssm.business.teacher.service;

import com.huasit.ssm.business.laboratory.entity.LaboratoryBook;
import com.huasit.ssm.business.laboratory.entity.LaboratoryBookRepository;
import com.huasit.ssm.business.teacher.entity.TeacherScore;
import com.huasit.ssm.business.teacher.entity.TeacherScoreRepository;
import com.huasit.ssm.core.user.entity.User;
import com.huasit.ssm.system.exception.SystemError;
import com.huasit.ssm.system.exception.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class TeacherScoreService {

    /**
     *
     */
    @Resource
    TeacherScoreRepository teacherScoreRepository;

    /**
     *
     */
    @Autowired
    LaboratoryBookRepository laboratoryBookRepository;

    /**
     *
     */
    public TeacherScore getById(Long id) {
        return this.teacherScoreRepository.findById(id).orElse(null);
    }
    /**
     *
     */
    public TeacherScore get(Long bookId, int bookHour, User loginUser) {
        return this.teacherScoreRepository.findByBookIdAndBookHourAndCreatorId(bookId, bookHour, loginUser.getId());
    }

    /**
     *
     */
    public Page<TeacherScore> list(TeacherScore form, int page, int pageSize, User loginUser) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Order.asc("id")));
        return this.teacherScoreRepository.findAll((Specification<TeacherScore>) (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(form.getTeacherId() != null) {
                predicates.add(cb.equal(root.get("teacherId").as(Long.class), form.getTeacherId()));
            }
            query.where(predicates.toArray(new Predicate[0]));
            return query.getRestriction();
        }, pageRequest);
    }

    /**
     *
     */
    public void save(TeacherScore form, User loginUser) {
        LaboratoryBook book = this.laboratoryBookRepository.findById(form.getBookId()).orElseThrow(() -> new SystemException(SystemError.FORMDATA_ERROR));
        if (book.getBookHour() != null && book.getBookHour() == form.getBookHour()) {
            if (!book.getTeacherId().equals(form.getTeacherId())) {
                throw new SystemException(SystemError.FORMDATA_ERROR);
            }
        } else if (book.getBookHour2() != null && book.getBookHour2() == form.getBookHour()) {
            if (!book.getTeacherId2().equals(form.getTeacherId())) {
                throw new SystemException(SystemError.FORMDATA_ERROR);
            }
        } else {
            throw new SystemException(SystemError.FORMDATA_ERROR);
        }
        TeacherScore db = this.teacherScoreRepository.findByBookIdAndBookHourAndCreatorId(form.getBookId(), form.getBookHour(), loginUser.getId());
        if (db == null) {
            db = new TeacherScore();
            db.setCreateTime(new Date());
            db.setCreatorId(loginUser.getId());
        }
        db.setTeacherId(form.getTeacherId());
        db.setBookId(form.getBookId());
        db.setBookHour(form.getBookHour());
        db.setDutyRate(form.getDutyRate());
        db.setMannerRate(form.getMannerRate());
        db.setTeachRate(form.getTeachRate());
        db.setComment(form.getComment());
        this.teacherScoreRepository.save(db);
    }

    /**
     *
     */
    public void delete(Long id, User loginUser) {
        TeacherScore db = this.getById(id);
        if (db == null) {
            return;
        }
        if (loginUser.getId().equals(1L) || db.getCreatorId().equals(loginUser.getId())) {
            this.teacherScoreRepository.deleteById(id);
        }
    }
}
