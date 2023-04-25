package com.cat.digital.reco.validators.email;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.regex.Pattern;

import com.amazonaws.util.StringUtils;
import com.cat.digital.reco.common.Constants;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ConditionalEmailValidator implements ConstraintValidator<ConditionalEmail, List<String>> {

  private Pattern emailPattern = Pattern.compile(Constants.EMAIL_PATTERN);

  @Override
  public void initialize(ConditionalEmail constraintAnnotation) {
  }

  @Override
  public boolean isValid(List<String> value, ConstraintValidatorContext context) {
    log.info("Validating email for not required fields or conditional");
    if (value == null || value.isEmpty()) {
      return true;
    } else {
      return  value.stream().filter(e -> !StringUtils.isNullOrEmpty(e) && emailPattern.matcher(e).matches()).count() == value.size();
    }

  }
}
