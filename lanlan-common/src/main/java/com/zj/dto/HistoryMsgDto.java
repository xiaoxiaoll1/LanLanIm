package com.zj.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaozj
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryMsgDto {

    @ApiModelProperty(name = "消息Id")
    private Long msgId;

    @ApiModelProperty(name = "发送者Id")
    private String fromUsername;

    @ApiModelProperty(name = "接收者Id")
    private String destUsername;

    @ApiModelProperty(name = "消息内容")
    private String content;

    @ApiModelProperty(name = "消息创建时间")
    private Long msgCreateTime;
}
