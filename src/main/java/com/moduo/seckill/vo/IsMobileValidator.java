package com.moduo.seckill.vo;

import com.alibaba.druid.util.StringUtils;
import com.moduo.seckill.util.ValidatorUtil;
import com.moduo.seckill.validator.IsMobile;
import org.springframework.context.annotation.Configuration;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 手机号码校验规则
 *
 * @author Wu Zicong
 * @create 2021-09-23 9:36
 */
public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {
  private boolean required = false;

  @Override
  public void initialize(IsMobile constraintAnnotation) {
    required = constraintAnnotation.required();
  }

  @Override
  public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
    if (required) {
      return ValidatorUtil.isMobile(s);
    } else {
      if (StringUtils.isEmpty(s)) { // 如果是非必填且为空，不校验
        return true;
      } else {
        return ValidatorUtil.isMobile(s);
      }
    }
  }
}
