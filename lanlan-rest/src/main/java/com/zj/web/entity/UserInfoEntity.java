package com.zj.web.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

/**
 * 用户信息表
 * @author xiaozj
 */
@ApiModel
@Entity
@Table(name = "user_info")
@Data
public class UserInfoEntity extends BaseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @ApiModelProperty(name = "用户名")
    @Column(name = "user_name")
    private String userName;

    @ApiModelProperty(name = "密码")
    @Column(name = "password")
    private String password;

}
