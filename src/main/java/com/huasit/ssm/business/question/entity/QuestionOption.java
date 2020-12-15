package com.huasit.ssm.business.question.entity;

import java.io.Serializable;

public class QuestionOption implements Serializable {

    /**
     *
     */
    private String answer;

    /**
     *
     */
    private boolean correct;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}
