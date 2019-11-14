package com.inz.z.note_book_annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/11/14 13:11.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
public @interface OperationLog {
    /**
     * 方法名
     */
    String methodName() default "";

    String describe() default "";

}
