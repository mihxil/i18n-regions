package org.meeuw.i18n.regions.validation.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Locale;
import java.util.Optional;
import java.util.logging.Logger;
import org.checkerframework.checker.nullness.qual.*;
import org.meeuw.i18n.languages.validation.LanguageValidationInfo;
import org.meeuw.i18n.regions.Region;
import org.meeuw.i18n.regions.RegionService;
import org.meeuw.i18n.regions.validation.Language;


/**
 * @author Michiel Meeuwissen
 * @since 3.0
 */
@Deprecated
public class LanguageValidator implements ConstraintValidator<Language, Object> {

    private static final Logger logger = Logger.getLogger(LanguageValidator.class.getName());
    
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
            try {
                if (value instanceof Locale) {
                    return isValid((Locale) value, value.toString());
                } else {
                    String svalue = String.valueOf(value);
                    if (annotation.forXml()) {
                        return isValid(Locale.forLanguageTag(svalue), svalue);
                    } else {

                        String[] split = splitAdapt(svalue, annotation.lenientCountry());
                        if (annotation.requireLowerCase()) {
                            if (! svalue.startsWith(split[0].toLowerCase())) {
                                return false;
                            }
                        }
                        return isValid(split[0], split[1], split[2], svalue);
                    }
                }
            } catch (IllegalArgumentException iae) {
                return false;
            }
        }
    }


    public static Locale adapt(@Nullable String v, boolean lenient) {
        return toLocale(splitAdapt(v, lenient));
    }

    private static Locale toLocale(String@Nullable[] split){
        if (split == null) {
            return null;
        }
        return new Locale(split[0], split[1], split[2]);
    }

    @Nullable
    private static String[] splitAdapt(@Nullable String v, boolean lenient) {
        if (v == null) {
            return null;
        }

        String[] split = v.split("[_-]", 3);

        String languageCode = split[0]; 

        String language = languageCode == null ? split[0] : languageCode.toLowerCase();
        switch (split.length) {
            case 1:
                return new String[]{language, "", ""};
            case 2:
                return new String[]{language, lenient ? split[1].toUpperCase() : split[1], ""};
            default:
                return new String[]{language, lenient ? split[1].toUpperCase() : split[1], split[2]};
        }
    }
    @RequiresNonNull("annotation")
    protected boolean isValid(Locale locale, String value) {
        return isValid(locale.getLanguage(), locale.getCountry(), locale.getVariant(),  value);
    }
    @RequiresNonNull("annotation")
    protected boolean isValid(String language, String country, String variant, String value) {
        if (! country.isEmpty()) {
            if (! annotation.mayContainCountry()) {
                return false;
            }
            Optional<Region> byCode = RegionService.getInstance().getByCode(country, annotation.lenientCountry());
            if (! byCode.isPresent()) {
                return false;
            } else {
                if (byCode.get().getType() != Region.Type.COUNTRY) {
                    return false;
                }
            }
        }
        
        LanguageValidationInfo info = new LanguageValidationInfo(
            annotation.lenientLanguage(),
            null,
            null,
            annotation.iso639_3(), 
            false,
            annotation.iso639_2(),
            false,
            annotation.requireLowerCase(),
            annotation.forXml(), 
            annotation.mayContainCountry(),
            annotation.mayContainVariant());
        
        
        return org.meeuw.i18n.languages.validation.impl.LanguageValidator.isValid(info, value);
    }
}
