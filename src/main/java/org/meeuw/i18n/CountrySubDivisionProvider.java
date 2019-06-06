package org.meeuw.i18n;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class CountrySubDivisionProvider implements RegionProvider<CountrySubDivision> {

    @Override
    public Optional<CountrySubDivision> getByCode(String code) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Stream<CountrySubDivision> values() {
        // TODO
        return Stream.empty();

    }
}
