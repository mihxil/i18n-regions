package org.meeuw.i18n;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import com.neovisionaries.i18n.CountryCode;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class CurrentCountryProvider implements RegionProvider<CurrentCountry> {

    @Override
    public Optional<CurrentCountry> getByCode(String code) {
        return Optional.ofNullable(CountryCode.getByCode(code)).map(CurrentCountry::new);
    }

    @Override
    public Stream<CurrentCountry> values() {
        return Arrays.stream(CountryCode.values()).map(CurrentCountry::new);

    }
}
