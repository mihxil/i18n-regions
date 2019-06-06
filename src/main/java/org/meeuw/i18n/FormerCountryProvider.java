package org.meeuw.i18n;

import java.util.Optional;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class FormerCountryProvider implements RegionProvider<FormerCountry> {

    @Override
    public Optional<FormerCountry> getByCode(String code) {
        return Optional.ofNullable(FormerlyAssignedCountryCode.getByCode(code)).map(FormerCountry::new);
    }
}
