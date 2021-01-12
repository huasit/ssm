package com.huasit.ssm.business.laboratory.entity;


import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Laboratory_Book")
public class LaboratoryBook {

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
    private boolean del;

    /**
     *
     */
    @Column(nullable = false)
    private Long userId;

    /**
     *
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "laboratoryId", nullable = false)
    private Laboratory laboratory;

    /**
     *
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date bookDay;

    /**
     *
     */
    @Column(nullable = false)
    private Integer bookHour;

    /**
     *
     */
    @Column(nullable = false)
    private Long teacherId;

    /**
     *
     */
    @Column
    private Integer bookHour2;

    /**
     *
     */
    @Column(nullable = false)
    private Long teacherId2;

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

    public boolean isDel() {
        return del;
    }

    public void setDel(boolean del) {
        this.del = del;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Laboratory getLaboratory() {
        return laboratory;
    }

    public void setLaboratory(Laboratory laboratory) {
        this.laboratory = laboratory;
    }

    public Date getBookDay() {
        return bookDay;
    }

    public void setBookDay(Date bookDay) {
        this.bookDay = bookDay;
    }

    public Integer getBookHour() {
        return bookHour;
    }

    public void setBookHour(Integer bookHour) {
        this.bookHour = bookHour;
    }

    public Integer getBookHour2() {
        return bookHour2;
    }

    public void setBookHour2(Integer bookHour2) {
        this.bookHour2 = bookHour2;
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

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public Long getTeacherId2() {
        return teacherId2;
    }

    public void setTeacherId2(Long teacherId2) {
        this.teacherId2 = teacherId2;
    }
}
