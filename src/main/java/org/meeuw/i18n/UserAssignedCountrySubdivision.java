package org.meeuw.i18n;

import java.util.Locale;

import javax.annotation.Nonnull;

import com.neovisionaries.i18n.CountryCode;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class UserAssignedCountrySubdivision implements Region {
    private static final long serialVersionUID = 0L;

    private final CountryCode countryCode;
    private final String code;
    private final String name;


    public UserAssignedCountrySubdivision(
        @Nonnull CountryCode countryCode,
        @Nonnull String code,
        @Nonnull String name) {
        this.countryCode = countryCode;
        this.code = code;
        this.name = name;
    }

    @Override
    public String getCode() {
        return countryCode.getAlpha2() + "-" + code;
    }

    @Override
    public Locale toLocale() {
        return countryCode.toLocale();

    }

    @Override
    public Type getType() {

        return Type.SUBDIVISION;
    }

    @Override
    public String getName() {
        return name;

    }


    @Override
    public String toString() {
        return getCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserAssignedCountrySubdivision that = (UserAssignedCountrySubdivision) o;

        if (countryCode != that.countryCode) return false;
        return code.equals(that.code);
    }

    @Override
    public int hashCode() {
        int result = countryCode.hashCode();
        result = 31 * result + code.hashCode();
        return result;
    }
}