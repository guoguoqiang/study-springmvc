package org.spring.study.springmvc.annotation;

import java.lang.annotation.*;

/**
 * @Author ggq
 * @Date 2020/11/6 14:24
 */

@Target(ElementType.TYPE)  //元注解
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Controller {
    String value() default "";
}
