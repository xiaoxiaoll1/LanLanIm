package com.zj.lib.intercepter;

import com.alibaba.fastjson.JSON;

import com.zj.exception.BusinessException;
import com.zj.exception.ExceptionEnum;
import com.zj.lib.annotation.AuthController;
import com.zj.bo.AuthResult;

import com.zj.lib.service.TokenService;
import com.zj.web.constant.LoginResponseCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;


/**
 * @author xiaozj
 */
@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Resource
    private TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Object controller = handlerMethod.getBean();
            printParameters(request, controller);
            // 访问需要登录的接口那些
            // 被@AuthController注解的Controller
            boolean present = controller.getClass().isAnnotationPresent(AuthController.class);
            if (!present) {
                return true;
            }

            String token = request.getHeader("token");
            if(!StringUtils.hasText(token)){
                throw new BusinessException(ExceptionEnum.BAD_REQUEST.getCode(), ExceptionEnum.BAD_REQUEST.getMsg());
            }
            Claims claims  = null;
            try {
                claims = tokenService.getClaimsFromToken(token);
            } catch (Exception e) {
                if (e instanceof ExpiredJwtException){
                    throw new BusinessException("token已过期,请重新认证获取");
                }
                throw new BusinessException("token非法不能解析:"+e.getMessage());
            }
            String username = (String) claims.get("username");
            if (!StringUtils.hasText(username)) {
                throw new BusinessException("token不包含账号信息,访问拒绝");
            }
            log.info("访问需要登录的接口(Authed)...........");
            if (!StringUtils.hasText(token)) {
                flushError(response, LoginResponseCode.TOKEN_IS_NULL, LoginResponseCode.TOKEN_IS_NULL_DESC);
                return false;
            }
            String tokenInRedis = tokenService.getToken(username);
            if (!StringUtils.hasText(tokenInRedis)) {
                flushError(response, LoginResponseCode.TOKEN_EXPIRED, LoginResponseCode.TOKEN_EXPIRED_DESC);
                return false;
            } else if (!tokenInRedis.equals(token)) {
                flushError(response, LoginResponseCode.TOKEN_INVALID, LoginResponseCode.TOKEN_INVALID_DESC);
                return false;
            }
        }

        boolean flag = isPermission(request, response);
        log.info("isPermission:" + flag);
        return flag;
    }

    private boolean isPermission(HttpServletRequest request, HttpServletResponse response) {
        return true;
    }

    /**
     * 输出请求参数
     *
     * @param request
     * @param controller
     */
    private void printParameters(HttpServletRequest request, Object controller) {
        StringBuilder paramsResponse = new StringBuilder();

        String controllerName = controller.getClass().getSimpleName();
        if (controllerName.contains("WebCache")) {
            return;
        }
        Map<String, String[]> paramsMap = request.getParameterMap();

        paramsResponse.append("request params :\n");

        for (Map.Entry<String, String[]> entry : paramsMap.entrySet()) {
            paramsResponse.append(entry.getKey() + ":" + entry.getValue()[0] + "\n");
        }
        log.info("请求前置拦截器参数:{}", paramsResponse);
    }

    private void flushError(HttpServletResponse res, String code, String desc) throws IOException {
        res.setCharacterEncoding("utf-8");
        res.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = res.getWriter();

        AuthResult authResult = new AuthResult();
        authResult.setCode(code);
        authResult.setCodeDesc(desc);
        String jsonStr = JSON.toJSONString(authResult);
        writer.append(jsonStr);
        writer.flush();
        writer.close();
    }
}
