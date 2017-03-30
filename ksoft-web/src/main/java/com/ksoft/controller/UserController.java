package com.ksoft.controller;

import com.ksoft.domain.admin.AdmUser;
import com.ksoft.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @author
 * @version 1.0 2016/10/10
 * @description
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/getUserInfo",method = RequestMethod.GET)
    @PreAuthorize("hasRole('USER')")
    public AdmUser getUserInfo(AdmUser user){
        AdmUser userInfo = new AdmUser();
        userInfo.setUser_name("LIQIN");
        userInfo.setPassword("123");
        return userInfo;
    }


    @RequestMapping(value = "/getAuth",method = RequestMethod.GET)
    @PreAuthorize("hasRole('ADMIN')")
    public AdmUser getAuth(AdmUser user){
        AdmUser userInfo = new AdmUser();
        //userInfo.setUserName("LIQIN");
        userInfo.setPassword("123");
        return userInfo;
    }

    @RequestMapping(value = "/auth", method= RequestMethod.GET)
    public AdmUser getAuth(Principal principal)  {
        return userService.findByUserName(principal.getName());
    }
}
