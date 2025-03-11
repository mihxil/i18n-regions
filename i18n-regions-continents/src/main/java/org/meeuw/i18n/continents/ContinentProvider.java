package org.meeuw.i18n.continents;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import jakarta.annotation.Priority;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.i18n.regions.spi.RegionProvider;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Priority(100)
public class ContinentProvider implements RegionProvider<Continent> {

    @Override
    public Optional<Continent> getByCode(@NonNull String code, boolean lenient) {
        try {
            if (code.startsWith(Continent.PREFIX)) {
                return Optional.of(new Continent(Continent.Code.valueOf(
                    code.substring(Continent.PREFIX.length())))
                );
            } else {
                return Optional.empty();
            }
        } catch (IllegalArgumentException iae) {
            return Optional.empty();
        }
    }

    @Override
    public Class<Continent> getProvidedClass() {
        return Continent.class;
    }

    @Override
    public Stream<Continent> values() {
        return Arrays.stream(Continent.Code.values()).map(Continent::new);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " (" + values().count() + " continents)";

    }
}
