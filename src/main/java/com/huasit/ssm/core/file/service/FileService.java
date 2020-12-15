package com.huasit.ssm.core.file.service;

import com.huasit.ssm.core.file.entity.File;
import com.huasit.ssm.core.file.entity.FileRepository;
import com.huasit.ssm.core.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class FileService {

    /**
     *
     */
    @Value("${storage.path}")
    private String storagePath;

    /**
     *
     */
    public File getFileById(Long id){
        return this.fileRepository.findFileById(id);
    }

    /**
     *
     */
    public File saveFile(String path, String name) {
        File file = new File();
        file.setDel(false);
        file.setCreateTime(new Date());
        file.setCreatorId(1L);
        file.setName(name);
        file.setPath(path);
        this.fileRepository.save(file);
        return file;
    }

    /**
     *
     */
    public List<File> getFileByIds(List<Long> ids){
        List<File> files = this.fileRepository.findByIds(ids);
        if(files != null) {
            for(File file : files) {
                file.setUrl("/file/download/?id=" + file.getId());
            }
        }
        return files;
    }

    /**
     *
     */
    public File upload(MultipartFile formFile, User loginUser) {
        File file = new File();
        file.setDel(false);
        file.setName(formFile.getOriginalFilename());
        file.setPath(this.stroageUploadFile(formFile));
        file.setCreatorId(loginUser.getId());
        file.setCreateTime(new Date());
        return this.fileRepository.save(file);
    }

    /**
     *
     */
    private String stroageUploadFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String path = "upload/" + UUID.randomUUID().toString() + "/";
        new java.io.File(storagePath + path).mkdirs();
        try {
            file.transferTo(new java.io.File(storagePath + path + fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return path + fileName;
    }

    public String getStoragePath() {
        return storagePath;
    }

    /**
     *
     */
    @Autowired
    FileRepository fileRepository;
}