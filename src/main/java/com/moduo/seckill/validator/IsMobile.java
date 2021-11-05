package com.moduo.seckill.validator;

import com.moduo.seckill.vo.IsMobileValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import java.lang.annotation.*;

/**
 * 验证手机号
 * @author Wu Zicong
 * @create 2021-09-23 9:33
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = {IsMobileValidator.class}
)
public @interface IsMobile {
    String message() default "{手机号码格式错误}";

    boolean required() default true;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
