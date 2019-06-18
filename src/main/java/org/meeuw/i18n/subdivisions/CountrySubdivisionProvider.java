package org.meeuw.i18n.subdivisions;

import be.olsson.i18n.subdivision.CountryCodeSubdivision;
import be.olsson.i18n.subdivision.SubdivisionFactory;
import com.neovisionaries.i18n.CountryCode;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.i18n.Region;
import org.meeuw.i18n.spi.RegionProvider;

import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class CountrySubdivisionProvider implements RegionProvider<CountrySubdivisionWithCode> {

    @Override
    public boolean canProvide(@NonNull Class<? extends Region> clazz) {
        return clazz.isAssignableFrom(CountrySubdivisionWithCode.class);

    }


    @Override
    public Optional<CountrySubdivisionWithCode> getByCode(@NonNull String code) {
        String[] countryAndSubDiversion = code.split("-", 2);
        if (countryAndSubDiversion.length < 2) {
            return Optional.empty();
        } else {
            CountryCode countryCode = CountryCode.getByAlpha2Code(countryAndSubDiversion[0]);
            if (countryCode == null) {
                return Optional.empty();
            }
            CountryCodeSubdivision subdivision = SubdivisionFactory.getSubdivision(countryCode, countryAndSubDiversion[1]);
            if (subdivision == null){
                return Optional.empty();
            }
            return Optional.of(new CountrySubdivisionWithCode(subdivision));

        }
    }

    @Override
    public Stream<CountrySubdivisionWithCode> values() {
        Spliterator<CountryCodeSubdivision> spliterator = new Spliterator<CountryCodeSubdivision>() {
            private int countryCode = 0;
            private Spliterator<CountryCodeSubdivision> spliterator;

            @Override
            public boolean tryAdvance(Consumer<? super CountryCodeSubdivision> action) {
                while(spliterator == null || ! spliterator.tryAdvance(action)) {
                    if (countryCode >=  CountryCode.values().length) {
                        return false;
                    }
                    List<CountryCodeSubdivision> subdivisions = SubdivisionFactory.getSubdivisions(CountryCode.values()[countryCode++]);

                    spliterator = subdivisions == null ? Spliterators.emptySpliterator() : subdivisions.spliterator();
                }
                return true;
            }

            @Override
            public Spliterator<CountryCodeSubdivision> trySplit() {
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
        return StreamSupport.stream(spliterator, false).map(CountrySubdivisionWithCode::new);
    }
}
