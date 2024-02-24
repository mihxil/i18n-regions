package org.meeuw.i18n.regions.validation;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import org.meeuw.i18n.regions.Region;
import org.meeuw.i18n.regions.RegionService;
import org.meeuw.i18n.regions.validation.impl.LanguageValidator;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * A validator for language codes (or {@link java.util.Locale}s (which also is a container for language codes)
 * <p>
 * This depends on {@link org.meeuw.i18n.languages.LanguageCode} for a list of all recognized ISO-639-3 codes.
 *
 * @author Michiel Meeuwissen
 * @since 0.3
 * @deprecated
 */
@Deprecated
@Target({FIELD, METHOD, TYPE_PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Constraint(validatedBy = LanguageValidator.class)
@Documented
public @interface Language {

    String message() default "{org.meeuw.i18n.regions.validation.language.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * Whether the locale may contain a country.
     * <p>
     * If so the country must then be found by {@link RegionService} and its type must be {@link Region.Type#COUNTRY}.
     */

    boolean mayContainCountry() default true;

     /**
     * Whether the local may contain a variant
     */
    boolean mayContainVariant() default false;

    /**
     * xml:lang uses '-' between language and country and is basically case-insensitive
     */
    boolean forXml() default true;

    /**
     * Will pass be passed as second argument to {@link RegionService#getByCode(String, boolean)}
     * This may make the country code case-insensitive (if not yet because of {@link #forXml()} and might e.g. also match the country codes on formerly assigned codes.
     */
    boolean lenientCountry() default false;


    /***
     * If the language is  not directly recognized, we'll check if the JVM can produce a display language for it.
     */
    boolean lenientLanguage() default false;
    
    /**
     * The default is to accept both ISO-639-1 and ISO-639-3 codes. If you want to restrict to ISO-639-1 only, set this to false.
     */
    boolean iso639_3() default true;

    /**
     * The default is to accept also part 2 codes.
     */
    boolean iso639_2() default true;

    boolean requireLowerCase() default true;


}
