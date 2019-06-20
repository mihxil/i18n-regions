package org.meeuw.i18n.bind.jaxb;

import java.util.stream.Stream;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.i18n.Region;
import org.meeuw.i18n.spi.RegionProvider;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
public class CountryProvider implements RegionProvider<Country> {
    @Override
    public boolean canProvide(@NonNull Class<? extends Region> clazz) {
        return clazz.isAssignableFrom(Country.class);

    }
    @Override
    public Stream<Country> values() {
        return Stream.of(
            new Country("NL", "Netherlands"),
            new Country("UK", "United Kingdom")
        );

    }
}
