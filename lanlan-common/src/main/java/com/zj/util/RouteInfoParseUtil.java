package com.zj.util;

import com.zj.bo.RouteInfo;
import com.zj.constant.ErrorConstant;
import com.zj.exception.BusinessException;

/**
 * @author xiaozj
 */
public class RouteInfoParseUtil {

    public static RouteInfo parse(String info){
        try {
            String[] serverInfo = info.split(":");
            // 由于服务端注册名为 ip:port
            RouteInfo routeInfo =  new RouteInfo(serverInfo[0], Integer.parseInt(serverInfo[1]));
            return routeInfo ;
        }catch (Exception e){
            throw new BusinessException(ErrorConstant.VALIDATION_FAIL) ;
        }
    }
}
