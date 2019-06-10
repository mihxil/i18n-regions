package org.meeuw.i18n;

import java.util.Optional;
import java.util.function.Predicate;

import com.neovisionaries.i18n.CountryCode;

/**
 * Represent a 'country', this can be a current (see {@link CurrentCountry} or former country (see {@link FormerCountry)}. It could also be some user defined country (see {@link UserAssignedCountry})
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public interface Country extends Region {

    Predicate<Region> CURRENT_OFFICIAL = (c) -> c instanceof CurrentCountry && ((CurrentCountry) c).getAssignment() == CountryCode.Assignment.OFFICIALLY_ASSIGNED;
    Predicate<Region> CURRENT_AND_FORMER = CURRENT_OFFICIAL.or(c -> c instanceof FormerCountry);
    Predicate<Region> CURRENT_AND_FORMER_AND_USER = CURRENT_AND_FORMER.or(c -> c instanceof UserAssignedCountry);

    static Optional<Country> getByCode(String code) {
        return Regions.getByCode(code, Country.class);
    }


    @Override
    default Type getType() {
        return Type.COUNTRY;
    }

    String getAlpha2();
    String getAlpha3();

    int getNumeric();

}
