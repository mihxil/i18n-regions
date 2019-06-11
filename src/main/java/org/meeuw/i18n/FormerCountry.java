package org.meeuw.i18n;

import java.time.Year;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.annotation.Nonnull;

import com.google.common.collect.Range;
import com.neovisionaries.i18n.CountryCode;

/**
 * Represents a 'former code', of which the code is defined by the enums of {@link FormerlyAssignedCountryCode}.
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class FormerCountry implements Country {
    private static final long serialVersionUID = 0L;

    private final FormerlyAssignedCountryCode code;

    public FormerCountry(@Nonnull FormerlyAssignedCountryCode code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code.getISO3166_3_Code();
    }

    @Override
    public Locale toLocale() {
        return code.toLocale();
    }

    @Override
    public String getName() {
        return code.getName();
    }
    @Override
    public String getName(Locale locale) {
        try {
            return ResourceBundle.getBundle("CountryCode", locale).getString(this.getCode());
        } catch (MissingResourceException mse){
            CountryCode currentCountry = CountryCode.valueOf(getAlpha2());
            return Country.of(currentCountry).getName(locale);
        }
    }

    public FormerlyAssignedCountryCode getCountryCode() {
        return code;
    }

    public Range<Year> getValidity() {
        return code.getValidity();
    }

    @Override
    public String toString() {
        return code.toString();
    }


    @Override
    public String getAlpha2() {
        return code.name().substring(0, 2);
    }
    @Override
    public String getAlpha3() {
        return null;
    }

    @Override
    public int getNumeric() {
        return code.getFormerCodes().stream().filter(c -> {
            try {
                Integer.parseInt(c);
                return true;
            } catch(NumberFormatException nfe) {
                return false;
            }
        }).map(Integer::parseInt).findFirst().orElse(-1);
    }

}
