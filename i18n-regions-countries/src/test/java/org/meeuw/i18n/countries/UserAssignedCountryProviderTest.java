package org.meeuw.i18n.countries;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.neovisionaries.i18n.CountryCode;
import com.neovisionaries.i18n.LanguageCode;

import static org.assertj.core.api.Assertions.assertThat;

public class UserAssignedCountryProviderTest {
    final UserAssignedCountryProvider inst = new UserAssignedCountryProvider();


    /**
     * Tests whether all User assigned countries in {@link CountryCode} have a corresponding instance.
     *
     * The only exception is {@link CountryCode#UNDEFINED} (We suggest {@link UserAssignedCountry#ZZ}
     *
     * Also tests that all {@link UserAssignedCountry}s are working without causing exceptions.
     */
    @Test
    void values() {
        Set<CountryCode> collect = Arrays.stream(CountryCode.values())
            .filter(a -> a.getAssignment() == CountryCode.Assignment.USER_ASSIGNED)
            .collect(Collectors.toSet());
        collect.remove(CountryCode.UNDEFINED);
        inst.values().forEach(country -> {
            System.out.println("" + country + " " + ((Country) country).getCountryCode() + " " + country.getName() + " " + country.getName(LanguageCode.nl));
            collect.removeIf(c -> c.equals(((Country) country).getCountryCode()));
        });
        assertThat(collect).isEmpty();;



    }
}
