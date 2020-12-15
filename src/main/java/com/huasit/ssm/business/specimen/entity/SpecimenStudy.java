package com.huasit.ssm.business.specimen.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Specimen_study")
public class SpecimenStudy {

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
    private Long userId;

    /**
     *
     */
    @Column(nullable = false)
    private Long specimenId;

    /**
     *
     */
    @Column(nullable = false)
    private int studyTiming;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSpecimenId() {
        return specimenId;
    }

    public void setSpecimenId(Long specimenId) {
        this.specimenId = specimenId;
    }

    public int getStudyTiming() {
        return studyTiming;
    }

    public void setStudyTiming(int studyTiming) {
        this.studyTiming = studyTiming;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
