package com.huasit.ssm.business.exam.service;

import com.huasit.ssm.business.exam.entity.ExamPaper;
import com.huasit.ssm.business.exam.entity.ExamPaperQuetionRepository;
import com.huasit.ssm.business.exam.entity.ExamPaperRepository;
import com.huasit.ssm.business.exam.entity.ExamRepository;
import com.huasit.ssm.business.term.service.TermService;
import com.huasit.ssm.core.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ExamPaperService {


    /**
     *
     */
    public Page<ExamPaper> list(ExamPaper form, int page, int pageSize, User loginUser) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Order.desc("id")));
        return this.examPaperRepository.findAll((Specification<ExamPaper>) (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("userId").as(Long.class), loginUser.getId()));
            query.where(predicates.toArray(new Predicate[0]));
            return query.getRestriction();
        }, pageRequest);
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
    ExamRepository examRepository;

    /**
     *
     */
    @Autowired
    ExamPaperRepository examPaperRepository;

    /**
     *
     */
    @Autowired
    ExamPaperQuetionRepository examPaperQuetionRepository;
}