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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;

@Service
@Transactional
public class ExamService {

    /**
     *
     */
    @Value("${exam.default.max_count}")
    private int defaultMaxCount;

    /**
     *
     */
    @Value("${exam.default.duration}")
    private int defaultDuration;

    /**
     *
     */
    @Value("${exam.default.question.count}")
    private int defaultQuestionCount;

    /**
     *
     */
    public void checkUserStudy(User loginUser) {
        if(loginUser.getType() == User.UserType.STUDENT) {
            int total = this.specimenStudyRepository.findStudyTimingByUserId(loginUser.getId());
            if(total < 3 * 60 * 60) {
                throw new SystemException(SystemError.EXAM_STUDY_TIMING_LIMIT, total / 60);
            }
        }
    }

    /**
     *
     */
    public Exam getExamWithCreate(User user) {
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
        this.examPaperRepository.save(paper);
        paper.setQuestions(this.buildExamPaperQuestion(exam, paper));
        return paper;
    }

    /**
     *
     */
    public void submitExam(ExamPaper form, User user) {
        ExamPaper paper = this.examPaperRepository.findById(form.getId()).orElseThrow(() -> new SystemException(SystemError.EXAM_DATA_ERROR));
        if (!paper.getUserId().equals(user.getId())) {
            throw new SystemException(SystemError.EXAM_DATA_ERROR);
        }
        List<ExamPaperQuestion> questions = this.examPaperQuetionRepository.findByPaperId(paper.getId());
        if (CollectionUtils.isEmpty(questions)) {
            throw new SystemException(SystemError.EXAM_DATA_ERROR);
        }
        paper.setSubmitTime(new Date());
        BigDecimal score = new BigDecimal(0);
        BigDecimal eachScore = new BigDecimal(100).divide(new BigDecimal(questions.size()));
        for (ExamPaperQuestion question : questions) {
            if (question.getCorrect() != null && question.getCorrect()) {
                score = score.add(eachScore);
            }
        }
        paper.setScore(score.floatValue());
        this.examPaperRepository.save(paper);
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
        Exam exam = new Exam();
        exam.setTerm(term);
        exam.setMaxCount(defaultMaxCount);
        exam.setDuration(defaultDuration);
        exam.setQuestionCount(defaultQuestionCount);
        exam.setCreateTime(new Date());
        this.examRepository.save(exam);
        return exam;
    }

    /**
     *
     */
    public ExamPaper getLastUnCompleteExamPaper(Exam exam, User user) {
        PageRequest pageRequest = PageRequest.of(0, 1, Sort.by(Sort.Order.desc("createTime")));
        Calendar overCheckDate = Calendar.getInstance();
        overCheckDate.setTime(new Date(new Date().getTime() - (exam.getDuration() * 1000)));
        Page<ExamPaper> page = this.examPaperRepository.findUnCompleteByUserIdAndExamId(user.getId(), exam.getId(), overCheckDate.getTime() , pageRequest);
        if (page.hasContent()) {
            return page.getContent().get(0);
        }
        return null;
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
        Map<Long, List<Question>> map = new HashMap<>();
        for (QuestionBank bank : banks) {
            List<Question> qs = this.questionRepository.findByBankId(bank.getId());
            Question q = qs.get(new Random().nextInt(qs.size()));
            ExamPaperQuestion question = new ExamPaperQuestion();
            question.setQid(q.getId());
            question.setPaperId(paper.getId());
            question.setTitle(q.getTitle());
            question.setOptions(q.getOptions());
            questions.add(question);
            qs.remove(q);
            map.put(bank.getId(), qs);
        }
        while (questions.size() < exam.getQuestionCount()) {
            QuestionBank bank = banks.get(new Random().nextInt(banks.size()));
            List<Question> qs = map.get(bank.getId());
            if (qs.size() == 0) {
                continue;
            }
            Question q = qs.get(new Random().nextInt(qs.size()));
            ExamPaperQuestion question = new ExamPaperQuestion();
            question.setQid(q.getId());
            question.setPaperId(paper.getId());
            question.setTitle(q.getTitle());
            question.setOptions(q.getOptions());
            questions.add(question);
            qs.remove(q);
        }
        this.examPaperQuetionRepository.saveAll(questions);
        return questions;
    }

    /**
     *
     */
    public void shuffleQuestion(ExamPaper paper) {
        if(CollectionUtils.isEmpty(paper.getQuestions())) {
            return;
        }
        for(ExamPaperQuestion question : paper.getQuestions()) {
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