package com.ksoft.dao;

import com.ksoft.domain.admin.AdmUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by d on 2017/3/30.
 */
@Transactional
public interface UserDao extends CrudRepository<AdmUser, Integer>, JpaRepository<AdmUser, Integer>,JpaSpecificationExecutor<AdmUser> {

    @Query(nativeQuery =true,value = "SELECT * FROM ADM_USERS WHERE USER_NAME = ?1")
    AdmUser findByUserName(String userName);
}
