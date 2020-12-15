package com.huasit.ssm.business.term.service;

import com.huasit.ssm.business.term.entity.Term;
import com.huasit.ssm.business.term.entity.TermRepository;
import com.huasit.ssm.system.exception.SystemError;
import com.huasit.ssm.system.exception.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class TermService implements ApplicationRunner {

    /**
     *
     */
    @Value("${term.default.from.01}")
    private String dateFrom01;

    /**
     *
     */
    @Value("${term.default.to.01}")
    private String dateTo01;

    /**
     *
     */
    @Value("${term.default.from.02}")
    private String dateFrom02;

    /**
     *
     */
    @Value("${term.default.to.02}")
    private String dateTo02;


    /**
     *
     */
    public Term getTermById(Long id) {
        return this.termRepository.findById(id).orElse(null);
    }

    /**
     *
     */
    public Term getTerm(int year, int num) {
        return this.termRepository.findByYearAndNum(year, num);
    }

    /**
     *
     */
    public Term getCurrentTerm() {
        Calendar calendar = Calendar.getInstance();
        List<Term> terms = this.termRepository.findByYear(calendar.get(Calendar.YEAR));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (Term term : terms) {
            try {
                Date now = dateFormat.parse(dateFormat.format(calendar.getTime()));
                if (!now.before(term.getDateFrom()) && !now.after(term.getDateTo())) {
                    return term;
                }
            } catch (ParseException e) {
                throw new SystemException(SystemError.TERM_ERROR);
            }
        }
        return null;
    }

    /**
     *
     */
    @Override
    public void run(ApplicationArguments applicationArguments) {
        List<Term> list = this.termRepository.findAll();
        if (list != null && list.size() > 0) {
            return;
        }
        list = new ArrayList<>();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        for (int n = year - 1; n < year + 20; n++) {
            list.add(new Term(n, 1, this.formatDefaultDate(dateFrom01, n), this.formatDefaultDate(dateTo01, n)));
            list.add(new Term(n, 2, this.formatDefaultDate(dateFrom02, n), this.formatDefaultDate(dateTo02, n)));
        }
        this.termRepository.saveAll(list);
    }

    /**
     *
     */
    private Date formatDefaultDate(String date, int year) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return dateFormat.parse(date.replaceAll("YEAR\\+", String.valueOf(year + 1)).replaceAll("YEAR", String.valueOf(year)));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     */
    @Autowired
    TermRepository termRepository;
}
