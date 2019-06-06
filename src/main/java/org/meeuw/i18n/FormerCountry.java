package org.meeuw.i18n;

import java.util.Locale;
import java.util.Optional;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class FormerCountry implements Country {
    private final FormerlyAssignedCountryCode code;

    public FormerCountry(FormerlyAssignedCountryCode code) {
        this.code = code;
    }

    @Override
    public String getISOCode() {
        return code.getName();
    }

    @Override
    public Locale toLocale() {
        return code.toLocale();
    }

    public FormerlyAssignedCountryCode getCode() {
        return code;
    }


    public static Optional<FormerCountry> getByCode(String code) {
        return Optional.of(FormerlyAssignedCountryCode.getByCode(code)).map(FormerCountry::new);
    }
}
