package org.meeuw.i18n.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.meeuw.i18n.Region;
import org.meeuw.i18n.validation.impl.RegionConstraintValidator;

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
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE, TYPE_PARAMETER })
@Retention(RUNTIME)
@Constraint(validatedBy = RegionConstraintValidator.class)
@Documented
public @interface ValidRegion {


    String message() default "{org.meeuw.i18n.validation.region.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * A list of codes to exclude. They are invalid regardless of {@link #classes()}
     */
    String[] excludes() default {};

    /**
     * A list of codes to include. They are valid (unless they are also in {@link #excludes()}, regardless of {@link #classes()}
     */
    String[] includes() default {};

    /**
     * A list of classes (extensions of {@link Region}) that are to be considered valid.
     * Defaults to {{@link Region}.class} (all regions are valid)
     */
    Class<? extends Region>[] classes() default {Region.class};

}
