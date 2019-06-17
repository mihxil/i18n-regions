package org.meeuw.i18n.continents;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import org.meeuw.i18n.Region;
import org.meeuw.i18n.RegionProvider;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class ContinentProvider implements RegionProvider<Continent> {
    @Override
    public boolean canProvide(@Nonnull Class<? extends Region> clazz) {
        return clazz.isAssignableFrom(Continent.class);
    }

    @Override
    public Optional<Continent> getByCode(@Nonnull String code) {
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
