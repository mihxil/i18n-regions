package org.meeuw.i18n.subdivisions;

import be.olsson.i18n.subdivision.CountryCodeSubdivision;
import be.olsson.i18n.subdivision.SubdivisionFactory;

import java.util.Locale;
import java.util.Optional;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.i18n.Region;
import org.meeuw.i18n.RegionService;

import com.neovisionaries.i18n.CountryCode;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public interface CountrySubdivision extends Region {

    static Optional<? extends CountrySubdivision>  of(
            @NonNull CountryCode countryCode,
            @NonNull String code) {
        CountryCodeSubdivision subdivision = SubdivisionFactory.getSubdivision(countryCode, code);
        if (subdivision != null) {
            return Optional.of(new CountrySubdivisionWithCode(subdivision));
        }
        return UserAssignedCountrySubdivision.of(countryCode, code);
    }


    String getCountryCode();

    @Override
    default String getBundle() {
        return "Subdivisions";
    }
    @Override
    default void toStringBuilder(@NonNull StringBuilder builder, @NonNull Locale locale) {
        Region.super.toStringBuilder(builder, locale);
        builder.append(" (");
        RegionService.getInstance().getByCode(getCountryCode()).ifPresent(c -> c.toStringBuilder(builder, locale));
        builder.append(")");
    }


    @Override
    default Type getType() {
        return Type.SUBDIVISION;
    }

}
