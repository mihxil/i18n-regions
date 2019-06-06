package org.meeuw.i18n;

import java.util.Optional;

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
}
