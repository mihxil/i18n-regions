package org.meeuw.i18n.subdivisions;

import java.util.Objects;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.i18n.countries.Country;
import org.meeuw.i18n.countries.CurrentCountry;
import org.meeuw.i18n.subdivisions.codes.CountrySubdivisionCode;

/**
 * A subdivision of a country. Backend by {@link CountryCodeSubdivision}.
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class CountrySubdivisionWithCode implements CountrySubdivision {
    private static final long serialVersionUID = 0L;

    private final CountrySubdivisionCode code;

    public CountrySubdivisionWithCode(@NonNull CountrySubdivisionCode code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code.getCode();
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

    public CountrySubdivisionCode getCountrySubdivisionCode() {
        return code;
    }

    @Override
    public Country getCountry() {
        return CurrentCountry.of(code.getCountryCode());
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

        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(code);
    }
}
