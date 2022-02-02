package org.meeuw.i18n.subdivisions;

import be.olsson.i18n.subdivision.CountryCodeSubdivision;
import be.olsson.i18n.subdivision.SubdivisionFactory;

import java.util.Locale;
import java.util.Optional;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.i18n.countries.Country;
import org.meeuw.i18n.countries.CurrentCountry;
import org.meeuw.i18n.regions.Region;
import org.meeuw.i18n.regions.RegionService;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public interface CountrySubdivision extends Region {

    static Optional<? extends CountrySubdivision>  of(
            @NonNull Country country,
            @NonNull String code) {
        if (country instanceof CurrentCountry) {
            CountryCodeSubdivision subdivision = SubdivisionFactory.getSubdivision(
                ((CurrentCountry) country).getCountryCode(),
                code
            );
            if (subdivision != null) {
                return Optional.of(new CountrySubdivisionWithCode(subdivision));
            }
        }
        return UserAssignedCountrySubdivision.of(country, code);
    }

    Country getCountry();

    default String getCountryCode() {
        return getCountry().getCode();
    }

    @Override
    default String getBundle() {
        return CountrySubdivision.class.getSimpleName();
    }
    @Override
    default void toStringBuilder(@NonNull StringBuilder builder, @NonNull Locale locale) {
        Region.super.toStringBuilder(builder, locale);
        builder.append(" (");
        Optional<Region> byCode = RegionService.getInstance()
            .getByCode(getCountryCode());
        if (byCode.isPresent()) {
            byCode.get().toStringBuilder(builder, locale);
        } else {
            builder.append(getCountryCode());
        }
        builder.append(")");
    }


    @Override
    default Type getType() {
        return Type.SUBDIVISION;
    }

}
