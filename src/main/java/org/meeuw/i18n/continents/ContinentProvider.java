package org.meeuw.i18n.continents;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.i18n.Region;
import org.meeuw.i18n.spi.RegionProvider;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class ContinentProvider implements RegionProvider<Continent> {
    @Override
    public boolean canProvide(@NonNull Class<? extends Region> clazz) {
        return clazz.isAssignableFrom(Continent.class);
    }

    @Override
    public Optional<Continent> getByCode(@NonNull String code) {
        try {
            if (code.startsWith(Continent.PREFIX)) {
                return Optional.of(new Continent(Continent.Code.valueOf(code.substring(Continent.PREFIX.length()))));
            } else {
                return Optional.empty();
            }
        } catch (IllegalArgumentException iae) {
            return Optional.empty();
        }

    }

    @Override
    public Stream<Continent> values() {
        return Arrays.stream(Continent.Code.values()).map(Continent::new);

    }
}
