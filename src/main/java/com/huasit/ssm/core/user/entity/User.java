package com.huasit.ssm.core.user.entity;

import com.huasit.ssm.business.term.entity.Term;
import com.huasit.ssm.core.permission.entity.Permission;
import com.huasit.ssm.core.role.entity.Role;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "SYS_USER")
public class User implements Serializable {

    public enum UserState {
        NORMAL, DELETE, DISABLE
    }

    public enum UserType {
        TEACHER, STUDENT, GUEST;
    }

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
    private UserState state;

    /**
     *
     */
    @Column(nullable = false)
    private boolean login;

    /**
     *
     */
    @Column(nullable = false)
    private UserType type;

    /**
     *
     */
    @Column(unique = true)
    private String username;

    /**
     *
     */
    @Column
    private String password;

    /**
     *
     */
    @Column(nullable = false)
    private String sex;

    /**
     *
     */
    @Column(nullable = false)
    private String name;

    /**
     *
     */
    @Column
    @Email
    private String email;

    /**
     *
     */
    @Column
    private String mobile;

    /**
     *
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "overTermId", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private Term overTerm;

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
    @Transient
    private List<Role> roles;

    /**
     *
     */
    @Transient
    private List<Permission> permissions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public UserState getState() {
        return state;
    }

    public void setState(UserState state) {
        this.state = state;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Term getOverTerm() {
        return overTerm;
    }

    public void setOverTerm(Term overTerm) {
        this.overTerm = overTerm;
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

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }
}
