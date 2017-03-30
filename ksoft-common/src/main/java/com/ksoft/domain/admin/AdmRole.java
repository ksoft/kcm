package com.ksoft.domain.admin;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by d on 2017/3/30.
 */
@Entity
@Data
@Table(name = "ADM_ROLES")
public class AdmRole implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "ROLE_CODE")
    private String role_code;
    @Column(name = "ROLE_NAME")
    private String role_name;

}
