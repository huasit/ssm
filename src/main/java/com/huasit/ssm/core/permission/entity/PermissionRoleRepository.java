package com.huasit.ssm.core.permission.entity;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

/**
 *
 */
@Transactional
public interface PermissionRoleRepository extends CrudRepository<PermissionRole, Long>, JpaSpecificationExecutor<PermissionRole>  {

}