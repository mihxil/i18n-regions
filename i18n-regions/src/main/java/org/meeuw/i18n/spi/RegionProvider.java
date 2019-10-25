package org.meeuw.i18n.spi;

import java.util.Optional;
import java.util.stream.Stream;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.meeuw.i18n.Region;
import org.meeuw.i18n.RegionService;

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
     * This is mainly need to optimize the implementation of {@link RegionService#getByCode(String, Class)}, which will not
     * use {@link #values()} of this class if it known that it would not result any matches.
     */

    default boolean canProvide(@NonNull Class<? extends Region> clazz) {
        return clazz.isAssignableFrom(getProvidedClass());
    }

    /**
     * Searches and returns region with given code. As an {@link Optional}, so it will return {@code Optional.empty()} if this provider does not provide a region with the given code
     */
    default Optional<T> getByCode(@NonNull String code, boolean lenient) {
        return values().filter(r -> r.getCode().equals(code)).findFirst();
    }
    default Optional<T> getByCode(@NonNull String code) {
        return getByCode(code, true);
    }

    default @Nullable T getByCodeOrNull(@NonNull String code) {
        return getByCode(code, true).orElse(null);
    }

    Class<T> getProvidedClass();

    /**
     * Returns all region instances provided by this provides. As a stream, so it can be filtered, mapped and collected easily according to the use of the caller.
     */
    Stream<T> values();

}
