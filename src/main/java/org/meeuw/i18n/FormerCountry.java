package org.meeuw.i18n;

import java.util.Locale;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class FormerCountry implements Country {
    private static final long serialVersionUID = 0L;

    private final FormerlyAssignedCountryCode code;

    public FormerCountry(FormerlyAssignedCountryCode code) {
        this.code = code;
    }

    @Override
    public String getISOCode() {
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

    public FormerlyAssignedCountryCode getCode() {
        return code;
    }

    @Override
    public String toString() {
        return code.toString();
    }

    @Override
    public String getISO3166_3_Code() {
        return code.getISO3166_3_Code();
    }
}
