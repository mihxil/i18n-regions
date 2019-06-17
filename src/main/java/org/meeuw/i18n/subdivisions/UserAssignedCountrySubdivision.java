package org.meeuw.i18n.subdivisions;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nonnull;

import org.meeuw.i18n.countries.Country;
import com.neovisionaries.i18n.CountryCode;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class UserAssignedCountrySubdivision implements CountrySubdivision {
    private static final long serialVersionUID = 0L;

    private static final Map<CountryCode, Map<String, UserAssignedCountrySubdivision>> CACHE = new ConcurrentHashMap<>();


    public static  Map<String, UserAssignedCountrySubdivision> ofCountry(@Nonnull CountryCode countryCode) {
        return CACHE.computeIfAbsent(countryCode, (cc) -> {
            Map<String, UserAssignedCountrySubdivision> value = new LinkedHashMap<>();
            Properties properties = new Properties();
            InputStream inputStream = UserAssignedCountrySubdivision.class.getResourceAsStream("/subdivisions." + cc.getAlpha2() + ".properties");
            if (inputStream != null) {
                try {
                    properties.load(inputStream);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            properties.forEach((k, v) -> {
                value.put((String) k , new UserAssignedCountrySubdivision(cc, (String) k, (String) v));
            });
            return Collections.unmodifiableMap(value);
            });
    }
    public static Optional<UserAssignedCountrySubdivision> of(@Nonnull CountryCode countryCode, String code) {
        return Optional.ofNullable(ofCountry(countryCode).get(code));
    }



    private final CountryCode countryCode;
    private final String code;
    private final String name;


    public UserAssignedCountrySubdivision(
        @Nonnull CountryCode countryCode,
        @Nonnull String code,
        @Nonnull String name) {
        this.countryCode = countryCode;
        this.code = code;
        this.name = name;
    }

    @Override
    public String getCode() {
        return countryCode.getAlpha2() + "-" + code;
    }

    @Override
    public Locale toLocale() {
        return countryCode.toLocale();

    }

    @Override
    public String getName() {
        return name;

    }


    @Override
    public Country getCountry() {
        return Country.of(countryCode);

    }

    @Override
    public String toString() {
        return getCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserAssignedCountrySubdivision that = (UserAssignedCountrySubdivision) o;

        if (countryCode != that.countryCode) return false;
        return code.equals(that.code);
    }

    @Override
    public int hashCode() {
        int result = countryCode.hashCode();
        result = 31 * result + code.hashCode();
        return result;
    }
}
