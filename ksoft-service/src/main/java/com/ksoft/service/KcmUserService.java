package com.ksoft.service;

import com.ksoft.domain.admin.AdmRole;
import com.ksoft.domain.admin.AdmUser;
import com.ksoft.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d on 2017/3/30.
 */
@Service
public class KcmUserService implements UserDetailsService { //自定义UserDetailsService 接口

    @Autowired
    UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) { //重写loadUserByUsername 方法获得 userdetails 类型用户

        AdmUser user = userDao.findByUserName(username);
        if(user == null){
            throw new UsernameNotFoundException("用户名不存在");
        }
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        //用于添加用户的权限。只要把用户权限添加到authorities 就万事大吉。
        for(AdmRole role:user.getRoles())
        {
            authorities.add(new SimpleGrantedAuthority(role.getRole_name()));
            System.out.println(role.getRole_name());
        }
        return new org.springframework.security.core.userdetails.User(user.getUser_name(),
                user.getPassword(), authorities);
    }
}
