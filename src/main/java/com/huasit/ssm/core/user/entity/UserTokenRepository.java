package com.huasit.ssm.core.user.entity;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

/**
 *
 */
@Transactional
public interface UserTokenRepository extends CrudRepository<UserToken, Long>  {

    /**
     *
     */
    @Query("from UserToken where enable=true and token=?1")
    UserToken findByToken(String token);
}