package org.meeuw.i18n.regions.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.meeuw.i18n.regions.Region;
import org.meeuw.i18n.regions.validation.impl.RegionConstraintValidator;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * A javax.validation annotation that can be used to restrict the values of a {@link Region} values.
 *
 * It recognized several types
 * If it is a Region or convertible to a region then it will be valid if the type and classes are as specified.
 *
 * Locale's are only validated if the 'country' part is filled, and then only be valid if the country is filled and recognized by the RegionsService and it's type is COUNTRY.
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


    String message() default "{org.meeuw.i18n.regions.validation.region.message}";

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
      * A list of types. The default empty array will allow for any type, or at least leave that unspecified. When validation {@link java.util.Locale} it may default to {@link Region.Type#COUNTRY}.
     */
    Region.Type[] types() default {};

    /**
     * A list of classes (extensions of {@link Region}) that are to be considered valid.
     * Defaults to {{@link Region}.class} (all regions are valid)
     */
    Class<? extends Region>[] classes() default {Region.class};

}
