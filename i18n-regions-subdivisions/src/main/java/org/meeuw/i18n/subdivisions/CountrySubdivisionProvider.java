package org.meeuw.i18n.subdivisions;

import be.olsson.i18n.subdivision.CountryCodeSubdivision;
import be.olsson.i18n.subdivision.SubdivisionFactory;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.meeuw.i18n.countries.Country;
import org.meeuw.i18n.countries.CurrentCountry;
import org.meeuw.i18n.regions.RegionService;
import org.meeuw.i18n.regions.spi.RegionProvider;

import com.neovisionaries.i18n.CountryCode;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class CountrySubdivisionProvider implements RegionProvider<CountrySubdivisionWithCode> {


    @Override
    public Optional<CountrySubdivisionWithCode> getByCode(@NonNull String code, boolean lenient) {
        if (lenient) {
            code = code.toUpperCase();
        }
        Optional<CountryAndSubdivisionCode> parsed = CountryAndSubdivisionCode.of(code);

        return parsed.map(p -> {
            if (! (p.getCountry() instanceof CurrentCountry)) {
                return null;
            }
            CountryCodeSubdivision subdivision = SubdivisionFactory.getSubdivision(((CurrentCountry)p.country).getCountryCode(), p.getSubdivision());
            if (subdivision == null){
                return null;
            }
            return new CountrySubdivisionWithCode(subdivision);
        });
    }


    @Override
    public Class<CountrySubdivisionWithCode> getProvidedClass() {
        return CountrySubdivisionWithCode.class;
    }

    @Override
    public Stream<CountrySubdivisionWithCode> values() {
        Spliterator<CountryCodeSubdivision> spliterator = new Spliterator<CountryCodeSubdivision>() {
            private int countryCode = 0;
            private Spliterator<CountryCodeSubdivision> spliterator;

            @Override
            public boolean tryAdvance(Consumer<? super CountryCodeSubdivision> action) {
                while (spliterator == null || !spliterator.tryAdvance(action)) {
                    if (countryCode >= CountryCode.values().length) {
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
                return IMMUTABLE | NONNULL;
            }
        };
        return StreamSupport.stream(spliterator, false).map(CountrySubdivisionWithCode::new);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " (" + values().count() + " subdivisions)";
    }

    public static class CountryAndSubdivisionCode {

        public static Optional<CountryAndSubdivisionCode> of(String code) {
            String[] countryAndSubDiversion = code.split("-", 2);
            if (countryAndSubDiversion.length < 2) {
                return Optional.empty();
            } else {
                Country country = RegionService.getInstance().getByCode(countryAndSubDiversion[0], Country.class).orElseThrow(() -> new IllegalArgumentException(countryAndSubDiversion[0]));
                return Optional.of(new CountryAndSubdivisionCode(country, countryAndSubDiversion[1]));
            }
        }


        private final Country country;

        private final String subdivision;

        private CountryAndSubdivisionCode(Country country, String subdivision) {
            this.country = country;
            this.subdivision = subdivision;
        }

        @Nullable
        public Country getCountry() {
            return country;
        }

        public String getSubdivision() {
            return subdivision;
        }

        @Override
        public String toString() {
            return country + ":" + subdivision;
        }
    }
}
