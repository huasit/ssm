package com.huasit.ssm.business.question.service;

import com.huasit.ssm.business.question.entity.*;
import com.huasit.ssm.core.user.entity.User;
import com.huasit.ssm.system.exception.SystemError;
import com.huasit.ssm.system.exception.SystemException;
import com.huasit.ssm.system.util.ExcelUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.io.File;
import java.util.*;

@Service
@Transactional
public class QuestionBankService implements ApplicationRunner {

    /**
     *
     */
    public Question getQuestionById(Long id) {
        return this.questionRepository.findById(id).orElse(null);
    }

    /**
     *
     */
    public Page<Question> list(Question form, int page, int pageSize, User loginUser) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Order.asc("orderIndex"), Sort.Order.asc("id")));
        return this.questionRepository.findAll((Specification<Question>) (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("bankId").as(Long.class), form.getBankId()));
            predicates.add(cb.equal(root.get("del").as(boolean.class), false));
            query.where(predicates.toArray(new Predicate[0]));
            return query.getRestriction();
        }, pageRequest);
    }

    /**
     *
     */
    public void add(Question form, User loginUser) {
        form.setCreateTime(new Date());
        form.setCreatorId(loginUser.getId());
        form.setId(null);
        form.setDel(false);
        this.questionRepository.save(form);
    }

    /**
     *
     */
    public void update(Long id, Question form, User loginUser) {
        Question db = this.questionRepository.findById(id).orElseThrow(() -> new SystemException(SystemError.FORMDATA_ERROR));
        form.setId(id);
        form.setDel(db.isDel());
        form.setCreatorId(db.getCreatorId());
        form.setCreateTime(db.getCreateTime());
        this.questionRepository.save(form);
    }

    /**
     *
     */
    public void delete(Long id, User loginUser) {
        this.questionRepository.updateDel(true, id);
    }

    /**
     *
     */
    public QuestionBank getQuestionBankById(Long id) {
        return this.questionBankRepository.findById(id).orElse(null);
    }

    /**
     *
     */
    public Page<QuestionBank> listBank(QuestionBank form, int page, int pageSize, User loginUser) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Order.asc("id")));
        return this.questionBankRepository.findAll((Specification<QuestionBank>) (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("del").as(boolean.class), false));
            query.where(predicates.toArray(new Predicate[0]));
            return query.getRestriction();
        }, pageRequest);
    }

    /**
     *
     */
    public void addBank(QuestionBank form, User loginUser) {
        form.setCreateTime(new Date());
        form.setCreatorId(loginUser.getId());
        form.setId(null);
        form.setDel(false);
        this.questionBankRepository.save(form);
    }

    /**
     *
     */
    public void updateBank(Long id, QuestionBank form, User loginUser) {
        QuestionBank db = this.questionBankRepository.findById(id).orElseThrow(() -> new SystemException(SystemError.FORMDATA_ERROR));
        form.setId(id);
        form.setDel(db.isDel());
        form.setCreatorId(db.getCreatorId());
        form.setCreateTime(db.getCreateTime());
        this.questionBankRepository.save(form);
    }

    /**
     *
     */
    public void deleteBank(Long id, User loginUser) {
        this.questionBankRepository.updateDel(true, id);
        this.questionRepository.updateDelByBankId(true, id);
    }

    /**
     *
     */
    @Override
    public void run(ApplicationArguments applicationArguments) {
        try {
            //this.importFromExcel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    public void importFromExcel() throws Exception {
        Map<String, Long> map = new HashMap<>();
        List<QuestionBank> questionBanks = this.questionBankRepository.findAll();
        for (QuestionBank questionBank : questionBanks) {
            map.put(questionBank.getName(), questionBank.getId());
        }
        File file = new File("C:\\Users\\sumin\\Downloads\\实验题库.xlsx");
        Workbook workbook = ExcelUtil.getWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0);
        Date now = new Date();
        for (int row = 2; row < Integer.MAX_VALUE; row++) {
            String index = ExcelUtil.getStringValue(sheet, row, 0);
            if (StringUtils.isEmpty(index)) {
                break;
            }
            String bank = ExcelUtil.getStringValue(sheet, row, 4);
            if (StringUtils.isEmpty(bank)) {
                continue;
            }
            if ("泌尿".equals(bank) || "生殖".equals(bank)) {
                bank = "泌尿生殖";
            }
            if ("感官".equals(bank) || "内分泌".equals(bank)) {
                bank = "感官和内分泌";
            }
            Long bankId = map.get(bank);
            for (int tx = 0; tx <= 1; tx++) {
                int start = 5;
                if(tx == 1) {
                    start = 11;
                }
                String title = ExcelUtil.getStringValue(sheet, row, start);
                String answer = ExcelUtil.getStringValue(sheet, row, start+1);
                Question question = new Question();
                question.setDel(false);
                question.setBankId(bankId);
                question.setTitle(title);
                question.setCreatorId(1L);
                question.setCreateTime(now);
                question.setOrderIndex(0);
                String[] as = new String[]{"A","B","C","D"};
                List<String> ass = Arrays.asList(as);
                List<QuestionOption> options = new ArrayList<>();
                for (int n = 0; n < 4; n++) {
                    String str = ExcelUtil.getStringValue(sheet, row, (start+2) + n);
                    QuestionOption option = new QuestionOption();
                    option.setAnswer(str);
                    option.setCorrect(n == ass.indexOf(answer));
                    options.add(option);
                }
                question.setOptions(options);
                this.questionRepository.save(question);
            }
        }
    }

    /**
     *
     */
    @Autowired
    QuestionRepository questionRepository;

    /**
     *
     */
    @Autowired
    QuestionBankRepository questionBankRepository;
}