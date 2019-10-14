package org.meeuw.i18n.bind.jaxb;

import java.util.stream.Stream;

import org.meeuw.i18n.spi.RegionProvider;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
public class CountryProvider implements RegionProvider<Country> {

    @Override
    public Class<Country> getProvidedClass() {
        return Country.class;

    }

    @Override
    public Stream<Country> values() {
        return Stream.of(
            new Country("NL", "Netherlands"),
            new Country("BE", "Belgium"),
            new Country("UK", "United Kingdom")
        );

    }
}
