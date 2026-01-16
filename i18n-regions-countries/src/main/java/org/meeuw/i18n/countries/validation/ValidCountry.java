package org.meeuw.i18n.countries.validation;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import org.meeuw.i18n.countries.Country;
import org.meeuw.i18n.countries.validation.impl.CountryConstraintValidator;
import org.meeuw.i18n.regions.Region;
import org.meeuw.i18n.regions.validation.ValidRegion;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * A jakarta.validation annotation that can be used to restrict the values of a {@link Country} value.
 * <p>
 * For example
 * <pre>
 *    {@literal @}ValidCountry(value = OFFICIAL | FORMER, includes = "ZZ")
 * </pre>
 *
 * So basically you specify one or more predicates, and/or a number of explicitly included and excluded codes.
 * <p>
 * When applied to instances of {@link Region}s that are not {@link Country} (or convertable to that), are considered valid. Use {@link org.meeuw.i18n.regions.validation.ValidRegion} to constraint that.
 * <p>
 * This annotation can also be applied to collections (though it is nicer to then put it on the element type), in which case all elements of the collection are validated.
 * <p>
 * It can also be applied to {@link CharSequence's}, in which case the {@link org.meeuw.i18n.regions.RegionService} is consulted.
 * <p>
 * Also {@link java.util.Locale}'s are supported, in which case the country part is validated. The language part is ignored. The language part can be validated
 * using {@code org.meeuw.i18n.languages.validation.ValidLanguage} (from {@code org.meeuw.i18n:i18n-iso-639})
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE, TYPE_PARAMETER })
@Retention(RUNTIME)
@Constraint(validatedBy = CountryConstraintValidator.class)
@Documented
public @interface ValidCountry {

    String message() default "{org.meeuw.i18n.validation.country.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

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



    /**
     * Base selection. Using a bitmap of the int constants in this class.
     */
    int value() default  ~0;

    /**
     * See {@link ValidRegion#excludes()}
     */
    String[] excludes() default {};


    /**
     * See {@link ValidRegion#excludeAssigners()}
     */
    String[] excludeAssigners() default {};

     /**
     * See {@link ValidRegion#includeAssigners()}
     */
    String[] includeAssigners() default {};

    /**
     * See {@link ValidRegion#includes()}
     */
    String[] includes() default {};

    /**
     * See {@link ValidRegion#types()}.
     */
    Region.Type [] types() default {Region.Type.COUNTRY};

    /**
     * See {@link ValidRegion#classes()}
     */
    Class<? extends Country>[] classes() default {Country.class};


    /**
     * See {@link ValidRegion#codes()}
     */
    String[] codes() default {};


}
