package org.meeuw.i18n;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import com.neovisionaries.i18n.CountryCode;

import javax.annotation.Nonnull;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class CurrentCountryProvider implements RegionProvider<CurrentCountry> {

    @Override
    public boolean canProvide(@Nonnull Class<? extends Region> clazz) {
        return clazz.isAssignableFrom(CurrentCountry.class);

    }

    @Override
    public Optional<CurrentCountry> getByCode(@Nonnull String code) {
        return Optional.ofNullable(CountryCode.getByCode(code)).map(CurrentCountry::new);
    }

    @Override
    public Stream<CurrentCountry> values() {
        return Arrays.stream(CountryCode.values()).map(CurrentCountry::new);

    }
}
