package com.cat.digital.reco.validators.implementations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.OffsetDateTime;
import java.util.Objects;

import com.cat.digital.reco.validators.interfaces.IExpirationDateValidator;

/**
 * Custom Spring validator class to validate the Expiration Date in Recommendation request.
 */
public class ExpirationDateValidator implements ConstraintValidator<IExpirationDateValidator, OffsetDateTime> {

  @Override
  public boolean isValid(final OffsetDateTime expirationTime,
                         final ConstraintValidatorContext constraintValidatorContext) {
    var currentTime = OffsetDateTime.now();
    var oneYearFromNow = currentTime.plusYears(1);
    return Objects.isNull(expirationTime) || expirationTime.isBefore(oneYearFromNow);
  }
}
