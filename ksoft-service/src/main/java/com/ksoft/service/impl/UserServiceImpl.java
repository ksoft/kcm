package com.ksoft.service.impl;

import com.ksoft.domain.admin.AdmUser;
import com.ksoft.dao.UserDao;
import com.ksoft.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Created by d on 2017/3/30.
 */
@Service
@Component
public class UserServiceImpl implements UserService{
    @Autowired
    private UserDao userDao;
    @Override
    public AdmUser findByUserName(String userName) {
        return userDao.findByUserName(userName);
    }
}
