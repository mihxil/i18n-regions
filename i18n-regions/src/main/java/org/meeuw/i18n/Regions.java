package org.meeuw.i18n;

import java.util.Comparator;
import java.util.Locale;

import javax.annotation.Priority;

import org.checkerframework.checker.nullness.qual.NonNull;

import com.neovisionaries.i18n.LanguageCode;

/**
 * Utilities related to {@link Region}s.
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class Regions {



    public static Comparator<Region> sortByName(Locale locale) {
        return Comparator.comparing(o -> o.getName(locale));
    }

    public static Comparator<Region> sortByName(LanguageCode language) {
        return sortByName(language.toLocale());
    }

    public static String toString(@NonNull  Region region, @NonNull  Locale language) {
        StringBuilder builder = new StringBuilder();
        toStringBuilder(builder, region, language);
        return builder.toString();
    }

    public static String toStringWithCode(@NonNull  Region region, @NonNull  Locale language) {
        StringBuilder builder = new StringBuilder();
        builder.append(region.getCode());
        builder.append(':');
        toStringBuilder(builder, region, language);
        return builder.toString();
    }

    public static String toStringWithCode(@NonNull  Region region, @NonNull  LanguageCode language) {
        return toStringWithCode(region, language.toLocale());
    }

    public static void toStringBuilder(@NonNull  StringBuilder builder, @NonNull  Region region, @NonNull  Locale language) {
        region.toStringBuilder(builder, language);


    }


    public static String toString(@NonNull  Region region, @NonNull  LanguageCode language) {
        return toString(region, language.toLocale());
    }

    public static <T> Comparator<T> priorityComparator() {
        return  (o1, o2) -> {
            final Priority p1 = o1.getClass().getAnnotation(Priority.class);
            final int v1 = p1 != null ? p1.value() : 100;
            final Priority p2 = o2.getClass().getAnnotation(Priority.class);
            final int v2 = p2 != null ? p2.value() : 100;
            return v1 - v2;
        };

    }
}
