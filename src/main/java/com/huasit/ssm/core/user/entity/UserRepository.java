package com.huasit.ssm.core.user.entity;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 *
 */
@Transactional
public interface UserRepository extends CrudRepository<User, Long>, JpaSpecificationExecutor<User> {

    List<User> findAll();

    List<User> findByUsernameIsNotNull();

    /**
     *
     */
    @Query("from User where id=?1")
    Optional<User> findById(Long id);

    /**
     *
     */
    @Query("from User where username=?1")
    User findByUsername(String username);

    /**
     *
     */
    @Modifying
    @Query("update User set state=?1 where id=?2")
    void updateState(User.UserState state, Long id);

    /**
     *
     */
    @Modifying
    @Query("update User set password=?2 where id=?1")
    void updatePassword(Long id, String password);

    @Modifying
    @Query("update User set state=2 where state=0")
    void disableNormal();

    @Modifying
    @Query("update User set state=0 where state=2")
    void normalDisable();
}