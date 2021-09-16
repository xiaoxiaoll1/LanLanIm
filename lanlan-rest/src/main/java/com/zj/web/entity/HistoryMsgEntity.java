package com.zj.web.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import javax.persistence.*;

/**
 * 离线消息实体
 * @author xiaozj
 */
@ApiModel
@Entity
@Table(name = "history_msg")
@Data
public class HistoryMsgEntity extends BaseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    /**
     * TODO 需建立msgId的唯一索引
     */
    @ApiModelProperty(name = "消息Id")
    @Column(name = "msg_id")
    private Long msgId;

    /**
     * TODO 需建立索引
     */
    @ApiModelProperty(name = "发送者Id")
    @Column(name = "from_username")
    private String fromUsername;

    /**
     * TODO 需建立索引
     */
    @ApiModelProperty(name = "接收者Id")
    @Column(name = "dest_username")
    private String destUsername;

    @ApiModelProperty(name = "消息内容")
    @Column(name = "content")
    private String content;

    @ApiModelProperty(name = "消息创建时间")
    @Column(name = "msg_create_time")
    private Long msgCreateTime;



}
