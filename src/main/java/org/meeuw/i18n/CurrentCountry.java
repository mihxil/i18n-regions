package org.meeuw.i18n;

import java.util.Locale;
import java.util.Optional;

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
    public String getName() {
        return code.getName();
    }

    @Override
    public Locale toLocale() {
        return code.toLocale();

    }
    public CountryCode getCode() {
        return code;
    }

    public static Optional<CurrentCountry> getByCode(String code) {
        return Optional.of(CountryCode.getByCode(code)).map(CurrentCountry::new);
    }
}
