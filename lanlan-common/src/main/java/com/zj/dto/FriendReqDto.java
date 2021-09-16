package com.zj.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaozj
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendReqDto {

    @ApiModelProperty(name = "请求人name")
    private String fromUsername;

    @ApiModelProperty(name = "接收人name")
    private String destUsername;

    @ApiModelProperty(name = "请求创建时间")
    private Long msgCreateTime;
}
