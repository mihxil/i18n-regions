package org.meeuw.i18n;

import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnull;


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

    public List<String> getFormerCodes() {
        return code.getFormerCodes();
    }

    @Override
    public Locale toLocale() {
        return code.toLocale();
    }

    @Override
    public String getName() {


        return code.getName();
    }

    public FormerlyAssignedCountryCode getCountryCode() {
        return code;
    }

    public ValidityRange getValidity() {
        return code.getValidity();
    }

    @Override
    public String toString() {
        return code.toString();
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
