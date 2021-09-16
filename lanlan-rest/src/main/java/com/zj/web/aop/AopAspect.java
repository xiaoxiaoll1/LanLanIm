package com.zj.web.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaozj
 */

@Component
@Aspect
@Slf4j
public class AopAspect {


    @Pointcut("execution(* com.zj.web.controller..*.*(..))")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object printLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        log.info("调用接口{}", getTargetMethod(proceedingJoinPoint));

        Map<String, Object> params = getNameAndValue(proceedingJoinPoint);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            log.info("传入参数类型: " + entry.getKey() + " 传入参数值: " + entry.getValue());
        }

        Object object = proceedingJoinPoint.proceed();  //执行连接点方法，object：方法返回值

        log.info("调用接口返回值{}", object.toString());

        return object;
    }
    /**
     * 获取参数Map集合
     * @param joinPoint
     * @return
     */
    Map<String, Object> getNameAndValue(ProceedingJoinPoint joinPoint) {
        Map<String, Object> param = new HashMap<>();
        Object[] paramValues = joinPoint.getArgs();
        String[] paramNames = ((CodeSignature)joinPoint.getSignature()).getParameterNames();
        for (int i = 0; i < paramNames.length; i++) {
            param.put(paramNames[i], paramValues[i]);
        }
        return param;
    }

    /**
     * 获取方法类全名+方法名
     */
    private String getTargetMethod(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        //获取目标类对象
        Class<?> aClass = joinPoint.getTarget().getClass();
        //获取方法签名信息,方法名和参数列表
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获取目标方法对象
        Method method = aClass.getDeclaredMethod(signature.getName(), signature.getParameterTypes());
        return method.getName();
    }







}
