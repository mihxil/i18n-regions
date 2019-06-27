package org.meeuw.i18n.subdivisions.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.meeuw.i18n.subdivisions.CountrySubdivision;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 *
  uded codes.
 *
 *
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Target({FIELD, METHOD, TYPE_PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = CountrySubdivisionConstraintValidator.class)
@Documented
public @interface ValidCountrySubdivision {


    String message() default "{org.meeuw.i18n.validation.countrysubdivision.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] excludes() default {};

    String[] includes() default {};

    String[] excludeCountries() default {};

    String[] includeCountries() default {};

    Class<? extends CountrySubdivision>[] classes() default {CountrySubdivision.class};


}
