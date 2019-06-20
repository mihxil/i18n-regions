package org.meeuw.i18n;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.i18n.spi.RegionProvider;

import com.neovisionaries.i18n.LanguageCode;

/**
 * Utilities related to {@link Region}s. This also implements the java {@link ServiceLoader}
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
}
