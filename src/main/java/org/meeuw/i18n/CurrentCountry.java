package org.meeuw.i18n;

import java.util.Locale;

import com.neovisionaries.i18n.CountryCode;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class CurrentCountry implements Country {
    private final CountryCode code;

    public CurrentCountry(CountryCode code) {
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

}
