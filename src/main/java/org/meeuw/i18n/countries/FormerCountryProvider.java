package org.meeuw.i18n.countries;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import org.meeuw.i18n.Region;
import org.meeuw.i18n.RegionProvider;
import org.meeuw.i18n.formerlyassigned.FormerlyAssignedCountryCode;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class FormerCountryProvider implements RegionProvider<FormerCountry> {

    @Override
    public boolean canProvide(@Nonnull Class<? extends Region> clazz) {
        return clazz.isAssignableFrom(FormerCountry.class);

    }


    @Override
    public Optional<FormerCountry> getByCode(@Nonnull String code) {
        return Optional.ofNullable(FormerlyAssignedCountryCode.getByCode(code)).map(FormerCountry::new);
    }

    @Override
    public Stream<FormerCountry> values() {
        return Arrays.stream(FormerlyAssignedCountryCode.values()).map(FormerCountry::new);

    }
}