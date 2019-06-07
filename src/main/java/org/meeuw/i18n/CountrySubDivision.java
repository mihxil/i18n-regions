package org.meeuw.i18n;

import be.olsson.i18n.subdivision.CountryCodeSubdivision;

import java.util.Locale;

import javax.annotation.Nonnull;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class CountrySubDivision implements Region {
    private static final long serialVersionUID = 0L;

    private final CountryCodeSubdivision code;

    public CountrySubDivision(@Nonnull CountryCodeSubdivision code) {
        this.code = code;
    }

    @Override
    public String getISOCode() {
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

    public CountryCodeSubdivision getCode() {
        return code;
    }

    @Override
    public String toString() {
        return code.toString();
    }

}
