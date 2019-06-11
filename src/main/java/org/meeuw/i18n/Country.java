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

    Predicate<Region> IS_OFFICIAL = (c) -> c instanceof CurrentCountry && ((CurrentCountry) c).getAssignment() == CountryCode.Assignment.OFFICIALLY_ASSIGNED;
    Predicate<Region> IS_FORMER = c -> c instanceof FormerCountry;
    Predicate<Region> IS_USER_ASSIGNED = c -> c instanceof UserAssignedCountry;

    static CurrentCountry of(CountryCode code) {
        return new CurrentCountry(code);
    }
    static FormerCountry of(FormerlyAssignedCountryCode code) {
        return new FormerCountry(code);
    }


    static Optional<Country> getByCode(String code) {
        return Regions.getByCode(code, Country.class);
    }


    @Override
    default Type getType() {
        return Type.COUNTRY;
    }

    int getNumeric();

}
