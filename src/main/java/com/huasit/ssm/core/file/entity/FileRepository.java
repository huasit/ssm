package com.huasit.ssm.core.file.entity;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

/**
 *
 */
@Transactional
public interface FileRepository extends CrudRepository<File, Long>, JpaSpecificationExecutor<File>  {

    /**
     *
     */
    @Query("from File where del=false and id=:id")
    File findFileById(@Param("id") Long id);

    /**
     *
     */
    @Query("from File where del=false and id in (:ids)")
    List<File> findByIds(@Param("ids") List<Long> ids);
}