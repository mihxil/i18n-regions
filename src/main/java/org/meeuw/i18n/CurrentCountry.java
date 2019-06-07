package org.meeuw.i18n;

import java.util.Locale;

import javax.annotation.Nonnull;

import com.neovisionaries.i18n.CountryCode;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class CurrentCountry implements Country {
    private static final long serialVersionUID = 0L;

    private final CountryCode code;

    public CurrentCountry(@Nonnull CountryCode code) {
        this.code = code;
    }

    @Override
    public String getISOCode() {
        return code.getAlpha2();
    }

    @Override
    public Locale toLocale() {
        return code.toLocale();

    }

    @Override
    public String getName() {
        return code.getName();
    }


    public CountryCode getCode() {
        return code;
    }

    @Override
    public String toString() {
        return code.toString();
    }

    @Override
    public String getISO3166_3_Code() {
        return code.getAlpha3();

    }
}
