package org.meeuw.i18n.subdivisions;

import com.neovisionaries.i18n.CountryCode;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.logging.Logger;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class UserAssignedCountrySubdivision implements CountrySubdivision {

    private static final Logger logger = Logger.getLogger(UserAssignedCountrySubdivision.class.getName());

    private static final long serialVersionUID = 0L;

    private static final Map<CountryCode, Map<String, UserAssignedCountrySubdivision>> CACHE = new ConcurrentHashMap<>();


    public static  Map<String, UserAssignedCountrySubdivision> ofCountry(@NonNull CountryCode countryCode) {
        logger.fine("Getting " + countryCode);
        return CACHE.computeIfAbsent(countryCode, (cc) -> {
            Map<String, UserAssignedCountrySubdivision> value = new LinkedHashMap<>();
            Properties properties = new Properties();
            String resource = "/org/meeuw/i18n/subdivisions/subdivisions." + cc.getAlpha2() + ".properties";
            InputStream inputStream = UserAssignedCountrySubdivision.class.getResourceAsStream(resource);
            if (inputStream != null) {
                try {
                    //logger.info("Loading " + resource);
                    properties.load(inputStream);
                    ForkJoinPool.commonPool().execute(() -> {
                        logger.info(() -> "Loaded " + resource);
                    });
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
    public static Optional<UserAssignedCountrySubdivision> of(@NonNull CountryCode countryCode, String code) {
        return Optional.ofNullable(ofCountry(countryCode).get(code));
    }


    private final CountryCode countryCode;
    private final String code;
    private final String name;


    public UserAssignedCountrySubdivision(
        @NonNull CountryCode countryCode,
        @NonNull String code,
        @NonNull String name) {
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
    public String getCountryCode() {
        return countryCode.name();

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
