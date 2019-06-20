package org.meeuw.i18n.spi;

import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

import javax.annotation.Priority;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.i18n.Region;
import org.meeuw.i18n.Regions;

/**
 * The provider which can be registered as a java service provider.
 *
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public interface RegionProvider<T extends Region> {

    /**
     * Wether this provided can create Regions of the specified type.
     *
     * This is mainly need to optimize the implementation of {@link Regions#getByCode(String, Class)}, which will not
     * use {@link #values()} of this class if it known that it would not result any matches.
     */

    boolean canProvide(@NonNull Class<? extends Region> clazz);

    /**
     * Searches and returns region with given code. As an {@link Optional}, so it will return {@code Optional.empty()} if this provider does not provide a region with the given code
     */
    default Optional<T> getByCode(@NonNull String code) {
        return values().filter(r -> r.getCode().equals(code)).findFirst();
    }

    /**
     * Returns all region instances provided by this provides. As a stream, so it can be filtered, mapped and collected easily according to the use of the caller.
     */
    Stream<T> values();



    Comparator<? super RegionProvider> COMPARATOR = (o1, o2) -> {


        final Priority p1 = o1.getClass().getAnnotation(Priority.class);
        final int v1 = p1 != null ? p1.value() : 100;
        final Priority p2 = o2.getClass().getAnnotation(Priority.class);
        final int v2 = p2 != null ? p2.value() : 100;
        return v1 - v2;
    };


}
