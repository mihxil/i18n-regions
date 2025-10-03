package org.meeuw.i18n.subdivisions;

import java.util.Locale;
import java.util.Optional;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.i18n.countries.Country;
import org.meeuw.i18n.countries.CurrentCountry;
import org.meeuw.i18n.countries.codes.CountryCode;
import org.meeuw.i18n.regions.Region;
import org.meeuw.i18n.regions.RegionService;
import org.meeuw.i18n.subdivisions.codes.CountrySubdivisionCode;
import org.meeuw.i18n.subdivisions.codes.SubdivisionFactory;



/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public interface CountrySubdivision extends Region {

    static Optional<? extends CountrySubdivision>  of(
        @NonNull Country country,
        @NonNull String code) {
        if (country instanceof CurrentCountry) {
            CountrySubdivisionCode subdivision = SubdivisionFactory.getSubdivision(
                country.getCode(),
                code
            ).orElse(null);
            if (subdivision != null) {
                return Optional.of(new CountrySubdivisionWithCode(subdivision));
            }
        }
        return UserAssignedCountrySubdivision.of(country, code);
    }

    static Optional<? extends CountrySubdivision>  of(
        @NonNull CountryCode country,
        @NonNull String code) {
        return of(CurrentCountry.of(country), code);
    }

    Country getCountry();

    @Override
    default Locale toLocale() {
        return getCountry().toLocale();
    }

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
