package org.meeuw.i18n.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;


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
@Constraint(validatedBy = RegionValidator.class)
@Documented
public @interface ValidRegion {


    String message() default "{org.meeuw.i18n.validation.region.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * Base selection. Using a bitmap of the int constants in this class.
     */
    int value() default  0;

    String[] excludes() default {};

    String[] includes() default {};

}
