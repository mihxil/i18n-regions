package org.meeuw.i18n.subdivisions;

import java.util.Collection;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.i18n.Region;
import org.meeuw.i18n.spi.RegionProvider;

import com.neovisionaries.i18n.CountryCode;

import static org.meeuw.i18n.subdivisions.UserAssignedCountrySubdivision.ofCountry;

/**
 * Defines subdivisions of countries via property files /subdivisions.&lt;alpha2 code of country&gt;.properties
 *
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class UserAssignedCountrySubdivisionProvider implements RegionProvider<UserAssignedCountrySubdivision> {


    @Override
    public boolean canProvide(@NonNull Class<? extends Region> clazz) {
        return clazz.isAssignableFrom(UserAssignedCountrySubdivision.class);

    }


    @Override
    public Optional<UserAssignedCountrySubdivision> getByCode(@NonNull String code, boolean lenient) {
        if (lenient) {

        }
        String[] countryAndSubDiversion = code.split("-", 2);
        if (countryAndSubDiversion.length < 2) {
            return Optional.empty();
        } else {
            CountryCode countryCode = CountryCode.getByAlpha2Code(countryAndSubDiversion[0]);
            if (countryCode == null) {
                return Optional.empty();
            }
            return Optional.ofNullable(ofCountry(countryCode).get(countryAndSubDiversion[1]));
        }
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
