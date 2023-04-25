package com.cat.digital.reco.validators.email;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.regex.Pattern;

import com.amazonaws.util.StringUtils;
import com.cat.digital.reco.common.Constants;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class EmailListValidator implements ConstraintValidator<ExtendedEmail, List<String>> {

  private Pattern emailPattern = Pattern.compile(Constants.EMAIL_PATTERN);

  @Override
  public void initialize(final ExtendedEmail constraintAnnotation) {
  }

  @Override
  public boolean isValid(final List<String> value, final ConstraintValidatorContext context) {
    log.info("Validation email for required fields.......");
    return !(value == null || value.isEmpty()) && value.stream().filter(e -> !StringUtils.isNullOrEmpty(e) && emailPattern.matcher(e).matches()).count() == value.size();
  }
}
