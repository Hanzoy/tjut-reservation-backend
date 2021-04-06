package com.hanzoy.tjutreservation.aop;

import java.lang.annotation.*;

@Inherited
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface API {
    String value();
    String remark();
}
