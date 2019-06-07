package org.meeuw.i18n;

import java.util.Optional;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class RegionsTest {

    @Test
    public void getCurrentByCode() {

        Optional<Region> nl = Regions.getByCode("NL");
        assertThat(nl).isPresent();
        assertThat(nl.get()).isInstanceOf(CurrentCountry.class);
        assertThat(nl.get().getISOCode()).isEqualTo("NL");
        assertThat(nl.get().getName()).isEqualTo("Netherlands");

    }
    @Test
    public void getFormerByCode() {

        Region cshh = Regions.getByCode("CSHH").orElse(null);
        assertThat(cshh).isNotNull();
        assertThat(cshh).isInstanceOf(FormerCountry.class);
        assertThat(cshh.getISOCode()).isEqualTo("CSHH");
        assertThat(cshh.getName()).isEqualTo("Czechoslovakia");
    }

    @Test
    public void getCountrySubDivision() {

        Region utrecht = Regions.getByCode("NL:UT").orElse(null);
        assertThat(utrecht).isNotNull();
        assertThat(utrecht).isInstanceOf(CountrySubDivision.class);
        assertThat(utrecht.getISOCode()).isEqualTo("NL:UT");
        assertThat(utrecht.getName()).isEqualTo("Utrecht");
    }


    @Test
    public void values() {

        Regions.values().forEach(r -> {
            System.out.println(r.getISOCode()  + " : " + r.getName());
        });

    }
}
