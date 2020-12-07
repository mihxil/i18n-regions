package org.meeuw.i18n.countries.validation;

import java.lang.annotation.*;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.meeuw.i18n.countries.Country;
import org.meeuw.i18n.countries.validation.impl.CountryConstraintValidator;
import org.meeuw.i18n.regions.Region;
import org.meeuw.i18n.regions.validation.ValidRegion;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * A javax.validation annotation that can be used to restrict the values of a {@link Country} value.
 *
 * For example
 * <pre>
 *    @ValidCountry(value = OFFICIAL | FORMER, includes = "ZZ")
 * </pre>
 *
 * So basicly you specify one or more predicates, and/or a number of explicitely included and excluded codes.
 *
 * When applied to instances of {@link Region}s that are not {@link Country} (or convertable to that), are considered valid. Use {@link org.meeuw.i18n.regions.validation.ValidRegion} to constraint that.
 *
 *
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


}
