package org.meeuw.i18n.regions.validation.impl;


import java.util.*;
import java.util.logging.Logger;
import java.util.spi.LocaleNameProvider;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.checkerframework.checker.nullness.qual.*;
import org.meeuw.i18n.regions.Region;
import org.meeuw.i18n.regions.RegionService;
import org.meeuw.i18n.regions.validation.Language;

import com.neovisionaries.i18n.LanguageAlpha3Code;
import com.neovisionaries.i18n.LanguageCode;


/**
 * @author Michiel Meeuwissen
 * @since 3.0
 */
public class LanguageValidator implements ConstraintValidator<Language, Object> {

    private static final Logger logger = Logger.getLogger(LanguageValidator.class.getName());

    public static final String[] LEGACY = {"iw", "ji", "in", "jw", "sh"};

    // http://www-01.sil.org/iso639-3/documentation.asp?id=zxx
    private static final Set<String> VALID_ISO_LANGUAGES = new HashSet<>();
    private static final Set<String> VALID_ISO3_LANGUAGES = new HashSet<>();

    private static final Set<String> RECOGNIZED_LANGUAGES = new HashSet<>();

    static {
        VALID_ISO_LANGUAGES.addAll(Arrays.asList(Locale.getISOLanguages()));
        for (LanguageAlpha3Code cod : LanguageAlpha3Code.values()) {
            VALID_ISO3_LANGUAGES.add(cod.toString());
            if (cod.getAlpha2() != null) {
                VALID_ISO_LANGUAGES.add(cod.getAlpha2().name());
            }
            VALID_ISO_LANGUAGES.addAll(Arrays.asList(LEGACY));
        }
        ServiceLoader<LocaleNameProvider> providers = ServiceLoader.load(LocaleNameProvider.class);
        providers.stream().forEach(p -> {
            Arrays.stream(p.get().getAvailableLocales())
                .map(Locale::getLanguage)
                .filter(l -> ! VALID_ISO_LANGUAGES.contains(l))
                .filter(l -> ! VALID_ISO3_LANGUAGES.contains(l))
                .filter(l -> ! RECOGNIZED_LANGUAGES.contains(l))
                .forEach(RECOGNIZED_LANGUAGES::add
            );
        });
        if (!RECOGNIZED_LANGUAGES.isEmpty()) {
            // TODO: never triggers, why not?
            logger.config(() -> "Recognized more languages: " + RECOGNIZED_LANGUAGES);
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
        LanguageCode languageCode = LanguageCode.getByCode(split[0], ! lenient);
        if (languageCode == null && ! lenient) {
            throw new IllegalArgumentException("Not a valid language " + split[0]);
        }
        String language = languageCode == null ? split[0] : languageCode.name().toLowerCase();
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
        return
            VALID_ISO3_LANGUAGES.contains(language) ||
                VALID_ISO_LANGUAGES.contains(language) ||
            RECOGNIZED_LANGUAGES.contains(language);

    }
}
