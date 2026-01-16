package org.meeuw.i18n.countries;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.meeuw.i18n.countries.codes.CountryCode;
import org.meeuw.i18n.languages.ISO_639_1_Code;

import static org.assertj.core.api.Assertions.assertThat;

public class UserAssignedCountryProviderTest {
    final UserAssignedCountryProvider inst = new UserAssignedCountryProvider();


    /**
     * Tests whether all User assigned countries in {@link CountryCode} have a corresponding instance.
     * <p>
     * The only exception is {@link CountryCode#UNDEFINED} (We suggest {@link UserAssignedCountry#ZZ}
     *
     * Also tests that all {@link UserAssignedCountry}s are working without causing exceptions.
     */
    @Test
    public void valuesAndRegister() {
        inst.register(new UserAssignedCountry("PNK", "Pinkeltjesland", "Dick Laan"));
        Set<CountryCode> collect = Arrays.stream(CountryCode.values())
            .filter(a -> a.getAssignment() == CountryCode.Assignment.USER_ASSIGNED)
            .collect(Collectors.toSet());
        collect.remove(CountryCode.UNDEFINED);
        inst.values().forEach(country -> {
            System.out.println(country + " " +
                ((Country) country).getCountryCode() + " " +
                country.getName() + " " +
                country.getName(ISO_639_1_Code.nl) + " " +
                country.getAlpha2() + " " +
                country.getAlpha3() + " " +
                country.getNumeric()
            );
            assertThat(inst.getProvidedClass().isInstance(country)).isTrue();
            assertThat(inst.getByCode(country.getCode().toLowerCase(), true).get()).isSameAs(country);
            collect.removeIf(c -> c.equals(((Country) country).getCountryCode()));
            assertThat(country.getCode().hashCode()).isEqualTo(country.hashCode());
        });
        assertThat(collect).isEmpty();;

        inst.register(new UserAssignedCountry("PNK", "Pinkelotjesland", "Dick Laan"));
        assertThat(inst.getByCode("PNK").get().getName()).isEqualTo("Pinkelotjesland");
    }

    /**
     * Make sure that all 'USER_ASSIGNED' codes are user assigned
     */
    @Test
    public void nv18n() {
        Arrays.stream(CountryCode.values())
            .filter(c -> c.getAssignment() == CountryCode.Assignment.USER_ASSIGNED).forEach(c -> {
            if (c != CountryCode.UNDEFINED) {
                assertThat(inst.getByCode(c.name())).withFailMessage("Not found " + c).isPresent();
            }
        });
    }
}
