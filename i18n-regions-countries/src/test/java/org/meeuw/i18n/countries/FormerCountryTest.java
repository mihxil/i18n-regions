package org.meeuw.i18n.countries;

import java.time.Year;

import org.junit.jupiter.api.Test;

import com.neovisionaries.i18n.LanguageCode;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class FormerCountryTest {
    final FormerCountryProvider impl = new FormerCountryProvider();
    @Test
    public void getValidity() {
        impl.values().forEach((country) -> {

            System.out.println("" + country + " " + country.getName(LanguageCode.nl) + " < " + country.getFormerCodes() + " " + country.getNumeric());
             // it wouldn't be former otherwise!
            assertThat(country.getValidity().contains(Year.now())).isFalse();
        });
    }



}
