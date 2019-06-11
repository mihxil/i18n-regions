package org.meeuw.i18n;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.neovisionaries.i18n.CountryCode;

/**
 * Defines subdivisions of countries via property files /subdivisions.&lt;alpha2 code of country&gt;.properties
 *
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class UserAssignedCountrySubDivisionProvider implements RegionProvider<UserAssignedCountrySubDivision> {

    private final Map<CountryCode, Map<String, UserAssignedCountrySubDivision>> cache = new ConcurrentHashMap<>();

    @Override
    public boolean canProvide(Class<? extends Region> clazz) {
        return clazz.isAssignableFrom(UserAssignedCountrySubDivision.class);

    }


    @Override
    public Optional<UserAssignedCountrySubDivision> getByCode(String code) {
        String[] countryAndSubDiversion = code.split("-", 2);
        if (countryAndSubDiversion.length < 2) {
            return Optional.empty();
        } else {
            CountryCode countryCode = CountryCode.getByAlpha2Code(countryAndSubDiversion[0]);
            return Optional.ofNullable(ofCountry(countryCode).get(countryAndSubDiversion[1]));
        }
    }

    protected Map<String, UserAssignedCountrySubDivision> ofCountry(CountryCode countryCode) {
        return cache.computeIfAbsent(countryCode, (cc) -> {
            Map<String, UserAssignedCountrySubDivision> value = new LinkedHashMap<>();
            Properties properties = new Properties();
            InputStream inputStream = getClass().getResourceAsStream("/subdivisions." + cc.getAlpha2() + ".properties");
            if (inputStream != null) {
                try {
                    properties.load(inputStream);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            properties.forEach((k, v) -> {
                value.put((String) k , new UserAssignedCountrySubDivision(cc, (String) k, (String) v));
            });
            return Collections.unmodifiableMap(value);
            });
    }

    @Override
    public Stream<UserAssignedCountrySubDivision> values() {
        Spliterator<UserAssignedCountrySubDivision> spliterator = new Spliterator<UserAssignedCountrySubDivision>() {
            private int countryCode = 0;
            private Spliterator<UserAssignedCountrySubDivision> spliterator;

            @Override
            public boolean tryAdvance(Consumer<? super UserAssignedCountrySubDivision> action) {
                while(spliterator == null || ! spliterator.tryAdvance(action)) {
                    if (countryCode >=  CountryCode.values().length) {
                        return false;
                    }
                    Collection<UserAssignedCountrySubDivision> subdivisions = ofCountry(CountryCode.values()[countryCode++]).values();

                    spliterator = subdivisions.spliterator();
                }
                return true;
            }

            @Override
            public Spliterator<UserAssignedCountrySubDivision> trySplit() {
                return null;

            }

            @Override
            public long estimateSize() {
                return Long.MAX_VALUE;

            }

            @Override
            public int characteristics() {
                return IMMUTABLE;

            }
        };
        return StreamSupport.stream(spliterator, false);
    }
}
