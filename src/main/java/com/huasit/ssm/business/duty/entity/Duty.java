package com.huasit.ssm.business.duty.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.huasit.ssm.core.user.entity.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "DUTY")
public class Duty {

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
    private Long laboratoryId;

    /**
     *
     */
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;

    /**
     *
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "amUserId")
    private User amUser;

    /**
     *
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pmUserId")
    private User pmUser;

    /**
     *
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ngUserId")
    private User ngUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLaboratoryId() {
        return laboratoryId;
    }

    public void setLaboratoryId(Long laboratoryId) {
        this.laboratoryId = laboratoryId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getAmUser() {
        return amUser;
    }

    public void setAmUser(User amUser) {
        this.amUser = amUser;
    }

    public User getPmUser() {
        return pmUser;
    }

    public void setPmUser(User pmUser) {
        this.pmUser = pmUser;
    }

    public User getNgUser() {
        return ngUser;
    }

    public void setNgUser(User ngUser) {
        this.ngUser = ngUser;
    }
}
