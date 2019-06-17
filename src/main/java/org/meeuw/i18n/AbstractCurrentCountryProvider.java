package org.meeuw.i18n;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import com.neovisionaries.i18n.CountryCode;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class AbstractCurrentCountryProvider implements RegionProvider<CurrentCountry> {

    private final Set<CountryCode.Assignment> assignments;

    protected AbstractCurrentCountryProvider(Set<CountryCode.Assignment> assignments) {
        this.assignments = assignments;
    }

    @Override
    public boolean canProvide(@Nonnull Class<? extends Region> clazz) {
        return clazz.isAssignableFrom(CurrentCountry.class);

    }

    @Override
    public Optional<CurrentCountry> getByCode(@Nonnull String code) {
        return Optional.ofNullable(CountryCode.getByCode(code)).filter(c -> assignments.contains(c.getAssignment())).map(CurrentCountry::new);
    }

    @Override
    public Stream<CurrentCountry> values() {
        return Arrays.stream(CountryCode.values()).filter(c -> assignments.contains(c.getAssignment())).map(CurrentCountry::new);

    }
}
