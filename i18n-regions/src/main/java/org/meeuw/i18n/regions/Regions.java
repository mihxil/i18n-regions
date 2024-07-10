package org.meeuw.i18n.regions;

import java.util.Comparator;
import java.util.Locale;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.i18n.languages.LanguageCode;

/**
 * Utilities related to {@link Region}s.
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class Regions {

    private Regions() {

    }


    /**
     * A {@link Comparator} to sort regions by {@link Region#getName(Locale)}, and hence in a certain language.
     * @param locale
     */
    public static Comparator<Region> sortByName(Locale locale) {
        return Comparator.comparing(o -> o.getName(locale));
    }

    /**
     * As {@link #sortByName(Locale)}, but with a {@link com.neovisionaries.i18n.LanguageCode } argument.
     * @deprecated
     * @see #sortByName(LanguageCode) 
     */
    @Deprecated
    public static Comparator<Region> sortByName(com.neovisionaries.i18n.LanguageCode language) {
        return sortByName(language.toLocale());
    }


    /**
     * As {@link #sortByName(Locale)}, but with a {@link LanguageCode } argument.
     */
    public static Comparator<Region> sortByName(@NonNull LanguageCode language) {
        return sortByName(language.toLocale());
    }


    /**
     * Utility for {@link Region#toStringBuilder(StringBuilder, Locale)} without the hassle of creating a {@link StringBuilder}
     */
    public static String toString(
        @NonNull Region region,
        @NonNull Locale language) {
        StringBuilder builder = new StringBuilder();
        region.toStringBuilder(builder, language);
        return builder.toString();
    }

    /**
     * As {@link #toString(Region, Locale)}, but with a {@link com.neovisionaries.i18n.LanguageCode} argument.
     * @deprecated
     */
    @Deprecated
    public static String toString(
        @NonNull  Region region,
        com.neovisionaries.i18n. @NonNull  LanguageCode language) {
        return toString(region, language.toLocale());
    }

    /**
     * As {@link #toString(Region, Locale)}, but with a {@link LanguageCode} argument.
     *
     */
    public static String toString(
        @NonNull  Region region,
        @NonNull LanguageCode language) {
        return toString(region, language.toLocale());
    }


    /**
     * As {@link Region#toStringBuilder(StringBuilder, Locale)} but  prefixed with the code.
     */
    public static String toStringWithCode(
        @NonNull Region region,
        @NonNull Locale language) {
        StringBuilder builder = new StringBuilder();
        builder.append(region.getCode());
        builder.append(':');
        region.toStringBuilder(builder, language);
        return builder.toString();
    }


    /**
     * As {@link #toStringWithCode(Region, Locale)} but with a {@link LanguageCode} argument.
     * @deprecated
     */
    @Deprecated
    public static String toStringWithCode(
        @NonNull Region region,
        com.neovisionaries.i18n. @NonNull LanguageCode  language) {
        return toStringWithCode(region, language.toLocale());
    }

       /**
     * As {@link #toStringWithCode(Region, Locale)} but with a {@link LanguageCode} argument.
     * @deprecated
     */
    @Deprecated
    public static String toStringWithCode(
        @NonNull Region region,
        @NonNull LanguageCode language) {
        return toStringWithCode(region, language.toLocale());
    }





}
