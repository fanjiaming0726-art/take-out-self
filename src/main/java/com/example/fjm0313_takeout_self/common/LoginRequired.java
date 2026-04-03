package com.example.fjm0313_takeout_self.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginRequired {
    // "CUSTOMER" 或 "EMPLOYEE"，标明这个接口需要哪种身份登录
    String value() default "CUSTOMER";
}