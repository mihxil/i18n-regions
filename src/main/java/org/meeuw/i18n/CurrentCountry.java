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
    public String getAlpha2() {
        return code.getAlpha2();

    }

    @Override
    public String getAlpha3() {
        return code.getAlpha3();
    }

    @Override
    public int getNumeric() {
        return code.getNumeric();

    }

    @Override
    public Locale toLocale() {
        return code.toLocale();

    }

    @Override
    public String getName(Locale locale) {
        if (code.getAssignment() == CountryCode.Assignment.OFFICIALLY_ASSIGNED) {
            return code.toLocale().getDisplayCountry(locale);
        } else {
            return Country.super.getName(locale);
        }
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
    public CountryCode.Assignment getAssignment() {
        return code.getAssignment();

    }
}
