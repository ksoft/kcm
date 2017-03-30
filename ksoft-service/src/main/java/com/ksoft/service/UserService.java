package com.ksoft.service;

import com.ksoft.domain.admin.AdmUser;

/**
 * Created by d on 2017/3/30.
 */
public interface UserService {
    AdmUser findByUserName(String userName);
}
