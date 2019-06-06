package org.meeuw.i18n;

import java.util.Optional;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class CountrySubDivisionProvider implements RegionProvider<CountrySubDivision> {

    @Override
    public Optional<CountrySubDivision> getByCode(String code) {
        return Optional.empty();
    }
}
