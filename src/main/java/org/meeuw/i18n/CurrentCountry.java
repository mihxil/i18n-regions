package org.meeuw.i18n;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.annotation.Nonnull;

import com.neovisionaries.i18n.CountryCode;

/**
 * Represents a country of which the code is one of the enum values of {@link CountryCode}.
 *
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
    public String getCode() {
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
        try {
            return ResourceBundle.getBundle("CountryCode", locale).getString(this.getCode());
        } catch (MissingResourceException mse){
            if (code.getAssignment() == CountryCode.Assignment.OFFICIALLY_ASSIGNED) {
                return code.toLocale().getDisplayCountry(locale);
            } else {
                return code.getName();
            }
        }
    }

    @Override
    public String getName() {
        return code.getName();
    }


    public CountryCode getCountryCode() {
        return code;
    }

    @Override
    public String toString() {
        return code.toString();
    }

    public CountryCode.Assignment getAssignment() {
        return code.getAssignment();

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CurrentCountry that = (CurrentCountry) o;

        return code == that.code;
    }

    @Override
    public int hashCode() {
        return code != null ? code.hashCode() : 0;
    }
}
