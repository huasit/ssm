package com.huasit.ssm.business.exam.service;

import com.huasit.ssm.business.exam.entity.*;
import com.huasit.ssm.business.question.entity.*;
import com.huasit.ssm.business.specimen.entity.SpecimenStudyRepository;
import com.huasit.ssm.business.term.entity.Term;
import com.huasit.ssm.business.term.service.TermService;
import com.huasit.ssm.core.user.entity.User;
import com.huasit.ssm.system.exception.SystemError;
import com.huasit.ssm.system.exception.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ExamService {

    /**
     *
     */
    private int defaultLearn = 3 * 3600;

    /**
     *
     */
    private int defaultDuration = 3600;

    /**
     *
     */
    private int defaultMaxCount = 4;

    /**
     *
     */
    public Exam getExamWithCreate() {
        Term term = this.termService.getCurrentTerm();
        Exam exam = this.getExamByTerm(term);
        if (exam == null) {
            exam = this.createExam(term);
        }
        return exam;
    }

    /**
     *
     */
    public Exam getExamById(Long id) {
        return this.examRepository.findById(id).orElse(null);
    }

    /**
     *
     */
    public Exam getExamByTerm(Term term) {
        return this.examRepository.findByTermId(term.getId());
    }


    /**
     *
     */
    public Exam createExam(Term term) {
        PageRequest pageRequest = PageRequest.of(0, 1);
        List<Exam> exams = this.examRepository.findLastExam(pageRequest);
        Exam exam = new Exam();
        if (exams == null || exams.size() <= 0) {
            exam.setLearn(defaultLearn);
            exam.setDuration(defaultDuration);
            exam.setMaxCount(defaultMaxCount);
        } else {
            exam.setLearn(exams.get(0).getLearn());
            exam.setDuration(exams.get(0).getDuration());
            exam.setMaxCount(exams.get(0).getMaxCount());
        }
        exam.setTerm(term);
        exam.setCreateTime(new Date());
        this.examRepository.save(exam);
        return exam;
    }

    /**
     *
     */
    public void updateExam(Long id, Exam form, User loginUser) {
        Exam db = this.getExamById(id);
        form.setId(db.getId());
        form.setTerm(db.getTerm());
        form.setCreateTime(db.getCreateTime());
        this.examRepository.save(form);
    }

    /**
     *
     */
    public void checkUserStudy(Exam exam, User loginUser) {
        if (loginUser.getType() == User.UserType.STUDENT) {
            Integer total = this.specimenStudyRepository.findStudyTimingByUserId(loginUser.getId());
            if (total == null) {
                total = 0;
            }
            if (total < exam.getLearn()) {
                throw new SystemException(SystemError.EXAM_STUDY_TIMING_LIMIT, total / 60);
            }
        }
    }


    /**
     *
     */
    public ExamPaper startExam(Exam exam, User user) {
        List<ExamPaper> examPapers = this.examPaperRepository.findByUserIdAndExamId(user.getId(), exam.getId());
        if (examPapers != null && examPapers.size() > exam.getMaxCount() - 1) {
            throw new SystemException(SystemError.EXAM_COUNT_LIMIT);
        }
        ExamPaper paper = new ExamPaper();
        paper.setUserId(user.getId());
        paper.setExamId(exam.getId());
        paper.setCreateTime(new Date());
        paper.setStartTime(new Date());
        paper.setQuestions(this.buildExamPaperQuestion(exam, paper));
        return paper;
    }

    /**
     *
     */
    public void submitExam(ExamPaper form, User user) {
        Exam exam = this.getExamById(form.getExamId());
        if (exam == null) {
            throw new SystemException(SystemError.EXAM_DATA_ERROR);
        }
        Date now = new Date();
        if (now.getTime() - form.getStartTime().getTime() > (exam.getDuration() * 1000 + 60)) {
            throw new SystemException(SystemError.EXAM_OVER_BUT_ANSWER);
        }
        form.setUserId(user.getId());
        form.setSubmitTime(now);
        BigDecimal score = new BigDecimal(0);
        BigDecimal eachScore = new BigDecimal(100).divide(new BigDecimal(form.getQuestions().size()));
        for (ExamPaperQuestion question : form.getQuestions()) {
            Question q = this.questionRepository.findById(question.getQid()).orElseThrow(() -> new SystemException(SystemError.EXAM_DATA_ERROR));
            question.setOptions(q.getOptions());
            question.setTitle(q.getTitle());
            question.setCorrect(false);
            for (QuestionOption option : question.getOptions()) {
                if (option.getAnswer().equals(question.getAnswer()) && option.isCorrect()) {
                    question.setCorrect(true);
                    break;
                }
            }
            if (question.getCorrect()) {
                score = score.add(eachScore);
            }
        }
        form.setScore(score.floatValue());
        this.examPaperRepository.save(form);
    }

    /**
     *
     */
    public void answerAQuestion(ExamPaperQuestion form, User user) {
        ExamPaperQuestion question = this.examPaperQuetionRepository.findById(form.getId()).orElseThrow(() -> new SystemException(SystemError.EXAM_DATA_ERROR));
        if (!question.getPaperId().equals(form.getPaperId())) {
            throw new SystemException(SystemError.EXAM_DATA_ERROR);
        }
        ExamPaper paper = this.examPaperRepository.findById(question.getPaperId()).orElseThrow(() -> new SystemException(SystemError.EXAM_DATA_ERROR));
        if (!paper.getUserId().equals(user.getId())) {
            throw new SystemException(SystemError.EXAM_DATA_ERROR);
        }
        Exam exam = this.examRepository.findById(paper.getExamId()).orElseThrow(() -> new SystemException(SystemError.EXAM_DATA_ERROR));
        if (new Date().getTime() > (paper.getStartTime().getTime() + exam.getDuration() * 1000)) {
            throw new SystemException(SystemError.EXAM_OVER_BUT_ANSWER);
        }
        String answer = form.getAnswer();
        boolean correct = false;
        for (QuestionOption option : question.getOptions()) {
            if (option.getAnswer().equals(answer) && option.isCorrect()) {
                correct = true;
                break;
            }
        }
        question.setCorrect(correct);
        question.setAnswer(form.getAnswer());
        question.setAnswerTime(new Date());
        this.examPaperQuetionRepository.save(question);
    }

    /**
     *
     */
    public List<ExamPaperQuestion> buildExamPaperQuestion(Exam exam, ExamPaper paper) {
        List<QuestionBank> banks = this.questionBankRepository.findWhichHasQuestions();
        if (CollectionUtils.isEmpty(banks)) {
            throw new SystemException(SystemError.EXAM_QUEST_BANK_COUNT_LIMIT);
        }
        List<ExamPaperQuestion> questions = new ArrayList<>();
        for (QuestionBank bank : banks) {
            List<Question> qs = this.questionRepository.findByBankIdByRandom(bank.getId(), bank.getQuestionCount());
            for (Question q : qs) {
                ExamPaperQuestion question = new ExamPaperQuestion();
                question.setQid(q.getId());
                question.setPaperId(paper.getId());
                question.setTitle(q.getTitle());
                question.setOptions(q.getOptions());
                questions.add(question);
            }
        }
        return questions;
    }

    /**
     *
     */
    public void shuffleQuestion(ExamPaper paper) {
        if (CollectionUtils.isEmpty(paper.getQuestions())) {
            return;
        }
        for (ExamPaperQuestion question : paper.getQuestions()) {
            Collections.shuffle(question.getOptions());
        }
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

    /**
     *
     */
    @Autowired
    SpecimenStudyRepository specimenStudyRepository;
}