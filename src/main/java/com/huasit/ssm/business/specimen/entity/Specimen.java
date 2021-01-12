package com.huasit.ssm.business.specimen.entity;

import com.huasit.ssm.core.file.entity.File;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Specimen")
public class Specimen {

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
    private Long bankId;

    /**
     *
     */
    @Column(nullable = false)
    private Long laboratoryId;

    /**
     *
     */
    @Column(nullable = false)
    private String code;

    /**
     *
     */
    @Column(nullable = false)
    private String name;

    /**
     *
     */
    @Column
    private String description;

    /**
     *
     */
    @Column
    private Long audioId;

    /**
     *
     */
    @Column
    private String imgIds;

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

    /**
     *
     */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="specimenId")
    private List<SpecimenKps> kps;

    /**
     *
     */
    @Transient
    private File audio;

    /**
     *
     */
    @Transient
    private long studyTiming;

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

    public Long getBankId() {
        return bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }

    public Long getLaboratoryId() {
        return laboratoryId;
    }

    public void setLaboratoryId(Long laboratoryId) {
        this.laboratoryId = laboratoryId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getAudioId() {
        return audioId;
    }

    public void setAudioId(Long audioId) {
        this.audioId = audioId;
    }

    public String getImgIds() {
        return imgIds;
    }

    public void setImgIds(String imgIds) {
        this.imgIds = imgIds;
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

    public List<SpecimenKps> getKps() {
        return kps;
    }

    public void setKps(List<SpecimenKps> kps) {
        this.kps = kps;
    }

    public File getAudio() {
        return audio;
    }

    public void setAudio(File audio) {
        this.audio = audio;
    }

    public long getStudyTiming() {
        return studyTiming;
    }

    public void setStudyTiming(long studyTiming) {
        this.studyTiming = studyTiming;
    }
}
