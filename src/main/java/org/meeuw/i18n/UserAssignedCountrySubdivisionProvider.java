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
public class UserAssignedCountrySubdivisionProvider implements RegionProvider<UserAssignedCountrySubdivision> {

    private final Map<CountryCode, Map<String, UserAssignedCountrySubdivision>> cache = new ConcurrentHashMap<>();

    @Override
    public boolean canProvide(Class<? extends Region> clazz) {
        return clazz.isAssignableFrom(UserAssignedCountrySubdivision.class);

    }


    @Override
    public Optional<UserAssignedCountrySubdivision> getByCode(String code) {
        String[] countryAndSubDiversion = code.split("-", 2);
        if (countryAndSubDiversion.length < 2) {
            return Optional.empty();
        } else {
            CountryCode countryCode = CountryCode.getByAlpha2Code(countryAndSubDiversion[0]);
            return Optional.ofNullable(ofCountry(countryCode).get(countryAndSubDiversion[1]));
        }
    }

    protected Map<String, UserAssignedCountrySubdivision> ofCountry(CountryCode countryCode) {
        return cache.computeIfAbsent(countryCode, (cc) -> {
            Map<String, UserAssignedCountrySubdivision> value = new LinkedHashMap<>();
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
                value.put((String) k , new UserAssignedCountrySubdivision(cc, (String) k, (String) v));
            });
            return Collections.unmodifiableMap(value);
            });
    }

    @Override
    public Stream<UserAssignedCountrySubdivision> values() {
        Spliterator<UserAssignedCountrySubdivision> spliterator = new Spliterator<UserAssignedCountrySubdivision>() {
            private int countryCode = 0;
            private Spliterator<UserAssignedCountrySubdivision> spliterator;

            @Override
            public boolean tryAdvance(Consumer<? super UserAssignedCountrySubdivision> action) {
                while(spliterator == null || ! spliterator.tryAdvance(action)) {
                    if (countryCode >=  CountryCode.values().length) {
                        return false;
                    }
                    Collection<UserAssignedCountrySubdivision> subdivisions = ofCountry(CountryCode.values()[countryCode++]).values();

                    spliterator = subdivisions.spliterator();
                }
                return true;
            }

            @Override
            public Spliterator<UserAssignedCountrySubdivision> trySplit() {
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
