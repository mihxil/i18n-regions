package org.meeuw.i18n.regions.validation.impl;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.checkerframework.checker.nullness.qual.*;
import org.meeuw.i18n.languages.LanguageCode;
import org.meeuw.i18n.regions.Region;
import org.meeuw.i18n.regions.RegionService;
import org.meeuw.i18n.regions.validation.Language;


/**
 * @author Michiel Meeuwissen
 * @since 3.0
 */
public class LanguageValidator implements ConstraintValidator<Language, Object> {

    private static final Logger logger = Logger.getLogger(LanguageValidator.class.getName());

    public static final String[] LEGACY = {"jw"}; // javanese?

    // http://www-01.sil.org/iso639-3/documentation.asp?id=zxx
    private static final Set<String> VALID_ISO_LANGUAGES;

    private static final Set<String> EXTRA_RECOGNIZED = ConcurrentHashMap.newKeySet();;


    static {
        Set<String> valid = new HashSet<>();
        valid.addAll(Arrays.asList(Locale.getISOLanguages()));
        valid.addAll(Arrays.asList(LEGACY));
        VALID_ISO_LANGUAGES = Collections.unmodifiableSet(valid);
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
            try {
                if (value instanceof Locale) {
                    return isValid((Locale) value);
                } else {
                    if (annotation.forXml()) {
                        return isValid(Locale.forLanguageTag(value.toString()));
                    } else {
                        String[] split = splitAdapt(String.valueOf(value), annotation.lenientCountry());
                        return isValid(split[0], split[1], split[2]);
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

        LanguageCode languageCode = LanguageCode.get(split[0]).orElse(null);
        if (languageCode == null && ! lenient) {
            throw new IllegalArgumentException("Not a valid language " + split[0]);
        }
        String language = languageCode == null ? split[0] : languageCode.getCode().toLowerCase();
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
    protected boolean isValid(Locale locale) {
        return isValid(locale.getLanguage(), locale.getCountry(), locale.getVariant());
    }
    @RequiresNonNull("annotation")
    protected boolean isValid(String language, String country, String variant) {
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
        if (! variant.isEmpty() && ! annotation.mayContainVariant()) {
            return false;
        }
        boolean recognized  = VALID_ISO_LANGUAGES.contains(language) ||
            (annotation.lenientLanguage() && EXTRA_RECOGNIZED.contains(language));

        if (! recognized) {
            Optional<LanguageCode> iso3 = LanguageCode.getByPart1(language);
            if (iso3.isPresent()){
                return true;
            }
            if (annotation.iso639_3()) {
                Optional<LanguageCode> isoPart1 = LanguageCode.getByCode(language);
                if (isoPart1.isPresent()){
                    return true;
                }
            }
            if (annotation.iso639_2()) {
                Optional<LanguageCode> isoPart2B = LanguageCode.getByPart2B(language);
                if (isoPart2B.isPresent()) {
                    return true;
                }

                Optional<LanguageCode> isoPart2T = LanguageCode.getByPart2T(language);
                if (isoPart2T.isPresent()) {
                    return true;
                }
            }
            if (annotation.lenientLanguage()) {
                String displayLanguage = new Locale(language).getDisplayLanguage();
                if (!language.equals(displayLanguage)) { // last fall back is iso code itself.
                    logger.info("Not a recognized language " + language + " -> " + displayLanguage + ", so recognized by the JMS. Will follow that");
                    EXTRA_RECOGNIZED.add(language);
                    return true;
                }
            }
        }
        return recognized;

    }
}
