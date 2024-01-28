package org.meeuw.i18n.countries;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import jakarta.annotation.Priority;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.i18n.formerlyassigned.FormerlyAssignedCountryCode;
import org.meeuw.i18n.regions.spi.RegionProvider;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Priority(10)
public class FormerCountryProvider implements RegionProvider<FormerCountry> {

    @Override
    public Optional<FormerCountry> getByCode(@NonNull String code, boolean lenient) {
        if (lenient) {
            return Optional.ofNullable(FormerlyAssignedCountryCode.getByCode(code)).map(FormerCountry::new);
        } else {
            try {
                return Optional.of(new FormerCountry(FormerlyAssignedCountryCode.valueOf(code)));
            } catch (IllegalArgumentException iae) {
                return Optional.empty();
            }
        }
    }

    @Override
    public Class<FormerCountry> getProvidedClass() {
        return FormerCountry.class;
    }

    @Override
    public Stream<FormerCountry> values() {
        return Arrays.stream(FormerlyAssignedCountryCode.values()).map(FormerCountry::new);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " (" + values().count() + " countries)";
    }

}
