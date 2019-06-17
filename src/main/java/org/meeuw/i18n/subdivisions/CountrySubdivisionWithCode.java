package org.meeuw.i18n.subdivisions;

import be.olsson.i18n.subdivision.CountryCodeSubdivision;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.i18n.countries.Country;

import java.util.Locale;

/**
 * A subdivision of a country. Backend by {@link CountryCodeSubdivision}.
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class CountrySubdivisionWithCode implements CountrySubdivision {
    private static final long serialVersionUID = 0L;

    private final CountryCodeSubdivision code;



    public CountrySubdivisionWithCode(@NonNull CountryCodeSubdivision code) {
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
    public Country getCountry() {
        return Country.of(code.getCountryCode());
    }

    @Override
    public String toString() {
        return code.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CountrySubdivisionWithCode that = (CountrySubdivisionWithCode) o;

        return code != null ? code.equals(that.code) : that.code == null;
    }

    @Override
    public int hashCode() {
        return code != null ? code.hashCode() : 0;
    }
}
