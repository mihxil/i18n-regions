package org.meeuw.i18n.regions.validation.impl;

import java.util.*;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.checkerframework.checker.nullness.qual.*;
import org.meeuw.i18n.regions.Region;
import org.meeuw.i18n.regions.RegionService;
import org.meeuw.i18n.regions.validation.Language;

import com.neovisionaries.i18n.LanguageAlpha3Code;


/**
 * @author Michiel Meeuwissen
 * @since 3.0
 */
public class LanguageValidator implements ConstraintValidator<Language, Object> {

    public static final String[] LEGACY = {"iw", "ji", "in", "jw"};

    // http://www-01.sil.org/iso639-3/documentation.asp?id=zxx
    private static final Set<String> VALID_ISO_LANGUAGES = new HashSet<>();
    private static final Set<String> VALID_ISO3_LANGUAGES = new HashSet<>();

    static {
        VALID_ISO_LANGUAGES.addAll(Arrays.asList(Locale.getISOLanguages()));
        for (LanguageAlpha3Code cod : LanguageAlpha3Code.values()) {
            VALID_ISO3_LANGUAGES.add(cod.toString());
            if (cod.getAlpha2() != null) {
                VALID_ISO_LANGUAGES.add(cod.getAlpha2().name());
            }
            VALID_ISO_LANGUAGES.addAll(Arrays.asList(LEGACY));
        }
    }

    @MonotonicNonNull
    Language annotation;

    @Override
    @EnsuresNonNull("annotation")
    public void initialize(@NonNull Language constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }

    @Override
    @RequiresNonNull("annotation")
    public boolean isValid(@Nullable Object value, @Nullable ConstraintValidatorContext context) {
        return isValid(value);
    }
    @RequiresNonNull("annotation")
    private boolean isValid(@Nullable Object value) {
        if (value == null) {
            return true;
        }
        if (value instanceof Iterable) {
            Iterable <?> i = (Iterable) value;
            for (Object o : i) {
                if (! isValid(o)) {
                    return false;
                }
            }
            return true;
        } else {
            Locale locale = toLocale(value);
            return isValid(locale);
        }
    }


    protected Locale toLocale(Object o) {
        if (o instanceof Locale) {
            return (Locale) o;
        } else {
            return Locale.forLanguageTag(o.toString());
        }
    }


    @RequiresNonNull("annotation")
    protected boolean isValid(Locale value) {
        if (! value.getCountry().isEmpty()) {
            if (! annotation.mayContainCountry()) {
                return false;
            }
            Optional<Region> byCode = RegionService.getInstance().getByCode(value.getCountry());
            if (! byCode.isPresent()) {
                return false;
            } else {
                if (byCode.get().getType() != Region.Type.COUNTRY) {
                    return false;
                }
            }

        }
        if (! value.getVariant().isEmpty() && ! annotation.mayContainVariant()) {
            return false;
        }
        return
            VALID_ISO3_LANGUAGES.contains(value.getLanguage()) ||
                VALID_ISO_LANGUAGES.contains(value.getLanguage());

    }
}
