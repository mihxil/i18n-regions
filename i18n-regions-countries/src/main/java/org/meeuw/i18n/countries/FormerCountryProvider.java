package org.meeuw.i18n.countries;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import javax.annotation.Priority;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.i18n.Region;
import org.meeuw.i18n.formerlyassigned.FormerlyAssignedCountryCode;
import org.meeuw.i18n.spi.RegionProvider;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Priority(10)
public class FormerCountryProvider implements RegionProvider<FormerCountry> {

    @Override
    public boolean canProvide(@NonNull Class<? extends Region> clazz) {
        return clazz.isAssignableFrom(FormerCountry.class);

    }


    @Override
    public Optional<FormerCountry> getByCode(@NonNull String code) {
        return Optional.ofNullable(FormerlyAssignedCountryCode.getByCode(code)).map(FormerCountry::new);
    }

    @Override
    public Stream<FormerCountry> values() {
        return Arrays.stream(FormerlyAssignedCountryCode.values()).map(FormerCountry::new);

    }
}
