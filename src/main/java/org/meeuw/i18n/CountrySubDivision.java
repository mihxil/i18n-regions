package org.meeuw.i18n;

import be.olsson.i18n.subdivision.CountryCodeSubdivision;

import java.util.Locale;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class CountrySubDivision implements Region {
    private final CountryCodeSubdivision code;

    public CountrySubDivision(CountryCodeSubdivision code) {
        this.code = code;
    }

    @Override
    public String getISOCode() {
        return code.getName();
    }

    @Override
    public Locale toLocale() {
        return code.getCountryCode().toLocale();

    }

    @Override
    public Type getType() {
        return Type.REGION;
    }

    public CountryCodeSubdivision getCode() {
        return code;
    }
}
