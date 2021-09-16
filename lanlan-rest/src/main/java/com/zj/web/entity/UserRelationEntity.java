package com.zj.web.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

/**
 * 用户关系实体
 * @author xiaozj
 */
@ApiModel
@Entity
@Table(name = "user_relation")
@Data
public class UserRelationEntity extends BaseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;


    /**
     * TODO 需建立 username1和username2的唯一索引
     */
    @ApiModelProperty(name = "用户名1")
    @Column(name = "user_name1")
    private String userName1;

    @ApiModelProperty(name = "用户名2")
    @Column(name = "user_name2")
    private String userName2;
}
