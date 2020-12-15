package com.huasit.ssm.business.exam.entity;

import com.huasit.ssm.business.question.entity.QuestionOption;
import com.huasit.ssm.system.util.JpaConverterListJson;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "EXAM_PAPER_QUESTION")
public class ExamPaperQuestion {

    /**
     *
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     *
     */
    @Column(nullable = false)
    private Long qid;

    /**
     *
     */
    @Column(nullable = false)
    private Long paperId;

    /**
     *
     */
    @Column
    private String title;

    /**
     *
     */
    @Convert(converter = JpaConverterListJson.class)
    @Column(columnDefinition = "TEXT")
    private List<QuestionOption> options;

    /**
     *
     */
    @Column
    private String answer;

    /**
     *
     */
    @Column
    private Date answerTime;

    /**
     *
     */
    @Column
    private Boolean correct;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQid() {
        return qid;
    }

    public void setQid(Long qid) {
        this.qid = qid;
    }

    public Long getPaperId() {
        return paperId;
    }

    public void setPaperId(Long paperId) {
        this.paperId = paperId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<QuestionOption> getOptions() {
        return options;
    }

    public void setOptions(List<QuestionOption> options) {
        this.options = options;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Date getAnswerTime() {
        return answerTime;
    }

    public void setAnswerTime(Date answerTime) {
        this.answerTime = answerTime;
    }

    public Boolean getCorrect() {
        return correct;
    }

    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }
}