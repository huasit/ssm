package com.huasit.ssm.business.specimen.service;

import com.huasit.ssm.business.attendance.service.AttendanceService;
import com.huasit.ssm.business.laboratory.entity.LaboratoryBook;
import com.huasit.ssm.business.laboratory.service.LaboratoryBookService;
import com.huasit.ssm.business.specimen.entity.Specimen;
import com.huasit.ssm.business.specimen.entity.SpecimenRepository;
import com.huasit.ssm.business.specimen.entity.SpecimenStudy;
import com.huasit.ssm.business.specimen.entity.SpecimenStudyRepository;
import com.huasit.ssm.core.file.entity.File;
import com.huasit.ssm.core.file.service.FileService;
import com.huasit.ssm.core.user.entity.User;
import com.huasit.ssm.system.exception.SystemError;
import com.huasit.ssm.system.exception.SystemException;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class SpecimenService implements ApplicationRunner {

    /**
     *
     */
    public Specimen study(Long specimenId, int interval, User loginUser) {
        Specimen specimen = this.getSpecimenById(specimenId);
        LaboratoryBook book = this.laboratoryBookService.getUserBook(loginUser);
        if (book == null && loginUser.getType() == User.UserType.STUDENT) {
            throw new SystemException(SystemError.LABORATORY_BOOK_HAS_NO_BOOK);
        }
        if(book != null) {
            boolean isAttendance = this.attendanceService.userIsAttendance(new Date(), book.getLaboratory().getId(), loginUser.getId());
            if(!isAttendance) {
                throw new SystemException(SystemError.ATTENDANCE_NOT_STUDY);
            }
            this.laboratoryBookService.updateBookTeacher(book);
        }
        String redisKey = String.format("specimen_study_user%d_specimen%d", loginUser.getId(), specimen.getId());
        String last = this.redisTemplate.opsForValue().get(redisKey);
        long current = System.currentTimeMillis();
        if (!StringUtil.isNullOrEmpty(last)) {
            if ((current - Long.parseLong(last)) / 1000 < interval) {
                return specimen;
            }
        }
        this.redisTemplate.opsForValue().set(redisKey, String.valueOf(current));
        SpecimenStudy study = this.specimenStudyRepository.findByUserIdAndSpecimenId(loginUser.getId(), specimen.getId());
        if (study == null) {
            study = new SpecimenStudy();
            study.setUserId(loginUser.getId());
            study.setSpecimenId(specimen.getId());
            study.setCreateTime(new Date());
            this.specimenStudyRepository.save(study);
        } else {
            this.specimenStudyRepository.updateStudyTiming(interval, loginUser.getId(), specimen.getId());
        }
        return specimen;
    }

    /**
     *
     */
    public int getStudyTiming(Long userId, Long specimenId) {
        SpecimenStudy study = this.specimenStudyRepository.findByUserIdAndSpecimenId(userId, specimenId);
        if(study == null) {
            return 0;
        }
        return study.getStudyTiming();
    }

    /**
     *
     */
    public Specimen getSpecimenById(Long id) {
        Specimen specimen = this.specimenRepository.findById(id).orElse(null);
        if (specimen == null) {
            return null;
        }
        specimen.setAudio(this.fileService.getFileById(specimen.getAudioId()));
        return specimen;
    }

    /**
     *
     */
    public Page<Specimen> list(Specimen form, int page, int pageSize, User loginUser) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Order.asc("id")));
        return this.specimenRepository.findAll((Specification<Specimen>) (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (form.getBankId() != null) {
                predicates.add(cb.equal(root.get("bankId").as(Long.class), form.getBankId()));
            }
            predicates.add(cb.equal(root.get("del").as(boolean.class), false));
            query.where(predicates.toArray(new Predicate[0]));
            return query.getRestriction();
        }, pageRequest);
    }

    /**
     *
     */
    public void add(Specimen form, User loginUser) {
        form.setCreateTime(new Date());
        form.setCreatorId(loginUser.getId());
        form.setId(null);
        form.setDel(false);
        this.specimenRepository.save(form);
    }

    /**
     *
     */
    public void update(Long id, Specimen form, User loginUser) {
        Specimen db = this.specimenRepository.findById(id).orElseThrow(() -> new SystemException(SystemError.FORMDATA_ERROR));
        form.setId(id);
        form.setDel(db.isDel());
        form.setCreatorId(db.getCreatorId());
        form.setCreateTime(db.getCreateTime());
        this.specimenRepository.save(form);
    }

    /**
     *
     */
    public void delete(Long id, User loginUser) {
        this.specimenRepository.updateDel(true, id);
    }


    /**
     *
     */
    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {

    }

    /**
     *
     */
    public void importImgIdsFromDir() throws Exception {
        String dir = "C:\\Users\\sumin\\Desktop\\图片资料-整理后";
        java.io.File dirFile = new java.io.File(dir);
        String[] filelist = dirFile.list();
        for (int i = 0; i < filelist.length; i++) {
            String fileName = filelist[i];
            Specimen specimen = this.specimenRepository.findByCode(fileName.split("-")[0]);
            if (specimen == null) {
                System.out.println(fileName);
            }
            File file = this.fileService.saveFile("special/" + fileName, fileName);
            String imgIds = specimen.getImgIds();
            if (imgIds == null) {
                imgIds = "";
            }
            imgIds = imgIds + file.getId() + ",";
            specimen.setImgIds(imgIds);
            this.specimenRepository.save(specimen);
        }
        System.out.println("ok");
    }

    /**
     *
     */
    public void importAudioIdFromDir() throws Exception {
        String dir = "C:\\Users\\sumin\\Desktop\\所有配音";
        java.io.File dirFile = new java.io.File(dir);
        String[] filelist = dirFile.list();
        for (int i = 0; i < filelist.length; i++) {
            String fileName = filelist[i];
            String fileNameNew = fileName.replace("—", "-");
            Specimen specimen = this.specimenRepository.findByCode(fileNameNew.split("-")[0]);
            if (specimen == null) {
                System.out.println(fileName);
            }
            File file = this.fileService.saveFile("special/" + fileName, fileName);
            specimen.setAudioId(file.getId());
            this.specimenRepository.save(specimen);
        }
        System.out.println("ok");
    }

    /**
     *
     */
    public List<Object[]> studyTimingList(User loginUser) {
        return this.specimenStudyRepository.findTiming(loginUser.getId());
    }

    /**
     *
     */
    @Autowired
    FileService fileService;

    /**
     *
     */
    @Autowired
    StringRedisTemplate redisTemplate;

    /**
     *
     */
    @Autowired
    AttendanceService attendanceService;

    /**
     *
     */
    @Autowired
    SpecimenRepository specimenRepository;

    /**
     *
     */
    @Autowired
    LaboratoryBookService laboratoryBookService;

    /**
     *
     */
    @Autowired
    SpecimenStudyRepository specimenStudyRepository;
}
