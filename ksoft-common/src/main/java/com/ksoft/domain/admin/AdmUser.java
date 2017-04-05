package com.ksoft.domain.admin;

import lombok.Data;
import javax.persistence.*;

import java.io.Serializable;
import java.util.List;

/**
 * Created by d on 2017/3/30.
 */
@Entity
@Data
@Table(name = "ADM_USERS")
public class AdmUser implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "USER_CODE")
    private String user_code;
    @Column(name = "USER_NAME")
    private String user_name;
    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "ROLE_CODE")
    private String role_code;
}
