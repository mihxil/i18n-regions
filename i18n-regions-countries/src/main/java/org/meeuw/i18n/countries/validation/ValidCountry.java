package org.meeuw.i18n.countries.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.meeuw.i18n.countries.Country;
import org.meeuw.i18n.countries.validation.impl.CountryConstraintValidator;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * A javax.validation annotation that can be used to restrict the values of a {@link org.meeuw.i18n.Region} (or {@link Country} value.
 *
 * For example
 * {@code
 *
 *    @ValidCountry(value = OFFICIAL | FORMER, includes = "ZZ")
 *
 * }
 *
 * So basicly you specify one or more predicates, and/or a number of explicitely included and excluded codes.
 *
 *
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Target({FIELD, METHOD, TYPE_PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = CountryConstraintValidator.class)
@Documented
public @interface ValidCountry {

    /**
     * See {@link Country#IS_OFFICIAL}
     */
    int OFFICIAL = 1 << 0;
    /**
     * See {@link Country#IS_FORMER}
     */
    int FORMER = 1 << 1;
    /**
     * See {@link Country#IS_USER_ASSIGNED}
     */
    int USER_ASSIGNED = 1 << 2;

    String message() default "{org.meeuw.i18n.validation.country.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * Base selection. Using a bitmap of the int constants in this class.
     */
    int value() default  ~0;

    String[] excludes() default {};

    String[] includes() default {};

    Class<? extends Country>[] classes() default {Country.class};


}
