package org.meeuw.i18n.subdivisions;

import be.olsson.i18n.subdivision.CountryCodeSubdivision;
import be.olsson.i18n.subdivision.SubdivisionFactory;

import java.util.Optional;

import javax.annotation.Nonnull;

import org.meeuw.i18n.countries.Country;
import org.meeuw.i18n.Region;
import com.neovisionaries.i18n.CountryCode;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public interface CountrySubdivision extends Region {

    static Optional<? extends CountrySubdivision>  of(
        @Nonnull CountryCode countryCode,
        @Nonnull String code) {
        CountryCodeSubdivision subdivision = SubdivisionFactory.getSubdivision(countryCode, code);
        if (subdivision != null) {
            return Optional.of(new CountrySubdivisionWithCode(subdivision));
        }
        return UserAssignedCountrySubdivision.of(countryCode, code);
    }


    Country getCountry();


    @Override
    default Type getType() {
        return Type.SUBDIVISION;
    }

}
