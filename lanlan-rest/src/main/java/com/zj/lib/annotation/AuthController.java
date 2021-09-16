package com.zj.lib.annotation;

import java.lang.annotation.*;

@Documented
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
/** 
* @author xiaozj
* 类说明 
* Controller需要Passport认证
*/
public @interface AuthController {

	boolean value() default true;
}
