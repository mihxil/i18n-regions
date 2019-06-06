package org.meeuw.i18n;

import java.util.Optional;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public interface Country extends Region {

    static Optional<? extends Country> getByCode(String code) {
        Optional<CurrentCountry> byCode = CurrentCountry.getByCode(code);
        if (byCode.isPresent()) {
            return byCode;
        } else {
            return FormerCountry.getByCode(code);
        }
    }

    @Override
    default Type getType() {
        return Type.COUNTRY;
    }
}
