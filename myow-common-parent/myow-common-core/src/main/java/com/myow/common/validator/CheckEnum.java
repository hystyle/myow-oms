package com.myow.common.validator;

import com.myow.common.enums.BaseEnum;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: yss
 * @date: 2026-01-28 22:44
 * @description: 自定义的属性校验注解
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumValidator.class)
public @interface CheckEnum {

    /**
     * 默认的错误提示信息
     *
     * @return String
     */
    String message();

    /**
     * 枚举类对象 必须实现BaseEnum接口
     */
    Class<? extends BaseEnum> value();

    /**
     * 是否必须
     *
     * @return boolean
     */
    boolean required() default false;

    //下面这两个属性必须添加 :不然会报错
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
