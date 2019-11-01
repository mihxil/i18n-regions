package org.meeuw.i18n.countries;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.i18n.regions.spi.RegionProvider;

import com.neovisionaries.i18n.CountryCode;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public abstract class AbstractCurrentCountryProvider implements RegionProvider<CurrentCountry> {

    private final Set<CountryCode.Assignment> assignments;

    protected AbstractCurrentCountryProvider(Set<CountryCode.Assignment> assignments) {
        this.assignments = assignments;
    }

    @Override
    public Class<CurrentCountry> getProvidedClass() {
        return CurrentCountry.class;
    }

    @Override
    public Optional<CurrentCountry> getByCode(@NonNull String code, boolean lenient) {
        return Optional.ofNullable(CountryCode.getByCode(code, ! lenient))
            .filter(c -> assignments.contains(c.getAssignment()))
            .map(CurrentCountry::new);
    }

    @Override
    public Stream<CurrentCountry> values() {
        return Arrays.stream(CountryCode.values())
            .filter(c -> assignments.contains(c.getAssignment()))
            .map(CurrentCountry::new);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " (" + values().count() + " countries)";

    }
}
