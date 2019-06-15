package org.meeuw.i18n;

import be.olsson.i18n.subdivision.CountryCodeSubdivision;
import be.olsson.i18n.subdivision.SubdivisionFactory;
import com.neovisionaries.i18n.CountryCode;

import javax.annotation.Nonnull;
import java.util.Locale;
import java.util.Optional;

/**
 * A subdivision of a country. Backend by {@link CountryCodeSubdivision}.
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class CountrySubdivision implements Region {
    private static final long serialVersionUID = 0L;

    private final CountryCodeSubdivision code;

    public static Optional<? extends Region> of(
        @Nonnull CountryCode countryCode,
        @Nonnull String code) {
        CountryCodeSubdivision subdivision = SubdivisionFactory.getSubdivision(countryCode, code);
        if (subdivision != null) {
            return Optional.of(new CountrySubdivision(subdivision));
        }
        return UserAssignedCountrySubdivision.of(countryCode, code);
    }

    public CountrySubdivision(@Nonnull CountryCodeSubdivision code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code.getCountryCode().getAlpha2() + "-" + code.getCode();
    }

    @Override
    public Locale toLocale() {
        return code.getCountryCode().toLocale();

    }

    @Override
    public Type getType() {
        // TODO,
        //return Type.valueOf(code.getType().name());
        return Type.SUBDIVISION;
    }

    @Override
    public String getName() {
        return code.getName();

    }

    public CountryCodeSubdivision getCountryCodeSubdivision() {
        return code;
    }

    @Override
    public String toString() {
        return code.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CountrySubdivision that = (CountrySubdivision) o;

        return code != null ? code.equals(that.code) : that.code == null;
    }

    @Override
    public int hashCode() {
        return code != null ? code.hashCode() : 0;
    }
}
