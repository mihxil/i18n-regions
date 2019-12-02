package org.meeuw.i18n.regions.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.meeuw.i18n.regions.Region;
import org.meeuw.i18n.regions.RegionService;
import org.meeuw.i18n.regions.validation.impl.LanguageValidator;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * A validator for language codes (or {@link java.util.Locale}s (which also is a container for language codes)
 *
 * @author Michiel Meeuwissen
 * @since 0.3
 */

@Target({FIELD, METHOD, TYPE_PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Constraint(validatedBy = LanguageValidator.class)
@Documented
public @interface Language {

    String message() default "{org.meeuw.i18n.regions.validation.language.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * Wether the locale may contain a country.
     *
     * If so the country must then be found by {@link RegionService} and its type must be {@link Region.Type#COUNTRY}.
     */

    boolean mayContainCountry() default true;

     /**
     * Wether the local may contain a variant
     */
    boolean mayContainVariant() default false;

}
