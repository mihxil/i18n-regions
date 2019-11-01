package org.meeuw.i18n.subdivisions.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.meeuw.i18n.regions.Region;
import org.meeuw.i18n.subdivisions.CountrySubdivision;
import org.meeuw.i18n.subdivisions.validation.impl.CountrySubdivisionConstraintValidator;
import org.meeuw.i18n.regions.validation.ValidRegion;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * Validates a value as a valid {@link CountrySubdivision}. If this is used on more generic value {@link Region} this might have be be combined with {@link ValidRegion} and/or other validator.
 *
 *
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE, TYPE_PARAMETER })
@Retention(RUNTIME)
@Constraint(validatedBy = CountrySubdivisionConstraintValidator.class)
@Documented
public @interface ValidCountrySubdivision {


    String message() default "{org.meeuw.i18n.validation.countrysubdivision.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


    Region.Type[] types() default {Region.Type.SUBDIVISION};

    /**
     * See {@link ValidRegion#excludes()}
     */
    String[] excludes() default {};

    /**
     * See {@link ValidRegion#includes()} ()}
     */
    String[] includes() default {};

    String[] excludeCountries() default {};

    String[] includeCountries() default {};

    Class<? extends CountrySubdivision>[] classes() default {CountrySubdivision.class};


}
