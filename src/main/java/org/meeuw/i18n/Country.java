package org.meeuw.i18n;

import java.util.Optional;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public interface Country extends Region {

    static Optional<? extends Country> getByCode(String code) {
      /*  Optional<CurrentCountry> byCode = ServiceLoader.load(RegionProvider.class);
        if (byCode.isPresent()) {
            return byCode;
        } else {
            return FormerCountry.getByCode(code);
        }*/
      return null;
    }

    @Override
    default Type getType() {
        return Type.COUNTRY;
    }
}
