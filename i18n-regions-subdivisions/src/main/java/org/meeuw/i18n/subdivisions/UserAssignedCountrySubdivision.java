package org.meeuw.i18n.subdivisions;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.i18n.countries.Country;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class UserAssignedCountrySubdivision implements CountrySubdivision {

    private static final Logger logger = Logger.getLogger(UserAssignedCountrySubdivision.class.getName());

    private static final long serialVersionUID = 0L;

    private static final Map<Country, Map<String, UserAssignedCountrySubdivision>> CACHE = new ConcurrentHashMap<>();


    public static  Map<String, UserAssignedCountrySubdivision> ofCountry(@NonNull Country country) {
        logger.fine("Getting " + country);
        return CACHE.computeIfAbsent(country, (cc) -> {
            final Map<String, UserAssignedCountrySubdivision> value = new LinkedHashMap<>();
            final Properties properties = new Properties();
            final String resource = "/org/meeuw/i18n/subdivisions/subdivisions." + cc.getCode() + ".properties";
            try (InputStream inputStream = UserAssignedCountrySubdivision.class.getResourceAsStream(resource)) {
                if (inputStream != null) {
                    //logger.info("Loading " + resource);
                    properties.load(inputStream);
                    new Thread(() -> {
                        logger.fine(() -> "Loaded " + resource);
                    }).start();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            properties.forEach((k, v) -> {
                value.put((String) k, new UserAssignedCountrySubdivision(cc, (String) k, (String) v));
            });
            return Collections.unmodifiableMap(value);
        });
    }
    public static Optional<UserAssignedCountrySubdivision> of(@NonNull Country country, String code) {
        return Optional.ofNullable(ofCountry(country).get(code));
    }

    private final Country country;
    private final String code;
    private final String name;


    public UserAssignedCountrySubdivision(
        @NonNull Country country,
        @NonNull String code,
        @NonNull String name) {
        this.country = country;
        this.code = code;
        this.name = name;
    }

    @Override
    public String getCode() {
        return country.getCode() + "-" + code;
    }

    @Override
    public Locale toLocale() {
        return country.toLocale();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Country getCountry() {
        return country;
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

        if (! Objects.equals(country, that.country)) return false;
        return code.equals(that.code);
    }

    @Override
    public int hashCode() {
        int result = country.hashCode();
        result = 31 * result + code.hashCode();
        return result;
    }
}
