package org.meeuw.i18n.countries;

import java.time.Year;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;
import org.meeuw.i18n.languages.ISO_639_1_Code;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class FormerCountryTest {
    final FormerCountryProvider impl = new FormerCountryProvider();

    @Test
    public void getValidity() {
        Set<FormerCountry> all = new HashSet<>();
        AtomicInteger count = new AtomicInteger(0);
        impl.values().forEach((country) -> {
            for (FormerCountry o : all) {
                assertThat(country.equals(o)).isFalse();
            }
            assertThat(all.add(country)).isTrue();

            count.incrementAndGet();

            StringBuilder builder = new StringBuilder();
            country.toStringBuilder(builder, Locale.ENGLISH);
            System.out.println(

                "" + builder + " (" + country + "," + country.toLocale() + ") " +
                    country.getName(ISO_639_1_Code.nl) +
                    " < " + country.getFormerCodes() +
                    " " + country.getNumeric());
             // it wouldn't be former otherwise!
            assertThat(country.getValidity().contains(Year.now())).isFalse();
        });
        assertThat(all.size()).isEqualTo(count.get());
    }

    @Test
    public void test() {

    }



}
