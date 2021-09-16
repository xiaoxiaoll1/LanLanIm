package com.zj.response;

import com.zj.constant.BusinessConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 实体类返回
 * @author xiaozj
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("unchecked")
public class ResultDataDto<D> {

    @ApiModelProperty(value = "返回信息")
    private String message;
    @ApiModelProperty(value = "返回状态码")
    private String code;
    @ApiModelProperty(value = "返回数据")
    private D data;


    public ResultDataDto<D> successResult(D data) {
        return new ResultDataDto<D>(BusinessConstant.SUCCESS_MSG, BusinessConstant.SUCCESS_CODE, data);
    }
}
