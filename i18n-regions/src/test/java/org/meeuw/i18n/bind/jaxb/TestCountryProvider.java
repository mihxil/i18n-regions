package org.meeuw.i18n.bind.jaxb;

import java.util.stream.Stream;

import org.meeuw.i18n.spi.RegionProvider;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
public class TestCountryProvider implements RegionProvider<TestCountry> {

    @Override
    public Class<TestCountry> getProvidedClass() {
        return TestCountry.class;

    }

    @Override
    public Stream<TestCountry> values() {
        return Stream.of(
            new TestCountry("NL", "Netherlands"),
            new TestCountry("BE", "Belgium"),
            new TestCountry("UK", "United Kingdom")
        );

    }
}
