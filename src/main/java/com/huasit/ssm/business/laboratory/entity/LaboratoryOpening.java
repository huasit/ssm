package com.huasit.ssm.business.laboratory.entity;

import javax.persistence.*;

@Entity
@Table(name = "LaboratoryOpening")
public class LaboratoryOpening {

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
    private String day;

    /**
     *
     */
    @Column(nullable = false)
    private String allowHours;

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

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getAllowHours() {
        return allowHours;
    }

    public void setAllowHours(String allowHours) {
        this.allowHours = allowHours;
    }
}
