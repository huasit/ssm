package com.huasit.ssm.business.teacher.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "Teacher_Score")
public class TeacherScore implements Serializable {

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
    private Long teacherId;

    /**
     *
     */
    @Column(nullable = false)
    private Long bookId;

    /**
     *
     */
    @Column(nullable = false)
    private int bookHour;

    /**
     *
     */
    @Column(nullable = false)
    private int dutyRate;

    /**
     *
     */
    @Column(nullable = false)
    private int mannerRate;

    /**
     *
     */
    @Column(nullable = false)
    private int teachRate;

    /**
     *
     */
    @Column(nullable = false)
    private String comment;

    /**
     *
     */
    @Column(nullable = false)
    private Long creatorId;

    /**
     *
     */
    @Column(nullable = false)
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public int getBookHour() {
        return bookHour;
    }

    public void setBookHour(int bookHour) {
        this.bookHour = bookHour;
    }

    public int getDutyRate() {
        return dutyRate;
    }

    public void setDutyRate(int dutyRate) {
        this.dutyRate = dutyRate;
    }

    public int getMannerRate() {
        return mannerRate;
    }

    public void setMannerRate(int mannerRate) {
        this.mannerRate = mannerRate;
    }

    public int getTeachRate() {
        return teachRate;
    }

    public void setTeachRate(int teachRate) {
        this.teachRate = teachRate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
