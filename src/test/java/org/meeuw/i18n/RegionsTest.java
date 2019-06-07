package org.meeuw.i18n;

import java.util.Locale;
import java.util.Optional;

import org.junit.Ignore;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@SuppressWarnings("OptionalGetWithoutIsPresent")
public class RegionsTest {

    @Test
    public void getCurrentByCode() {

        Optional<Region> nl = Regions.getByCode("NL");
        assertThat(nl).isPresent();
        assertThat(nl.get()).isInstanceOf(CurrentCountry.class);
        assertThat(nl.get().getISOCode()).isEqualTo("NL");
        assertThat(nl.get().getName()).isEqualTo("Netherlands");
        assertThat(nl.get().getName(new Locale("nl"))).isEqualTo("Nederland");

    }

    @Test
    public void getCurrentByCodeAsCountry() {
        Optional<CurrentCountry> nl = Regions.getByCode("NL", CurrentCountry.class);
        assertThat(nl.get().getAlpha3()).isEqualTo("NLD");
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
    public void getFormerByCodeAsCountry() {
        Optional<Country> nl = Regions.getByCode("CSHH", Country.class);
        assertThat(nl.get().getISOCode()).isEqualTo("CSHH");
        assertThat(nl.get().getAlpha2()).isEqualTo("CS");
    }

    @Test
    public void getCountrySubDivisionUtrecht() {

        Region utrecht = Regions.getByCode("NL-UT").orElse(null);
        assertThat(utrecht).isNotNull();
        assertThat(utrecht).isInstanceOf(CountrySubDivision.class);
        assertThat(utrecht.getISOCode()).isEqualTo("NL-UT");
        assertThat(utrecht.getName()).isEqualTo("Utrecht");
    }

    @Test
    @Ignore("This is missing in subdivision, make pull request")
    public void getCountrySubDivisionGreatBritain() {

        Region gbn = Regions.getByCode("GB-GBN").orElse(null);
        assertThat(gbn).isNotNull();
        assertThat(gbn).isInstanceOf(CountrySubDivision.class);
        assertThat(gbn.getISOCode()).isEqualTo("NL-UT");
        assertThat(gbn.getName()).isEqualTo("Utrecht");
    }


    @Test
    public void values() {

        Regions.values().forEach(r -> {
            System.out.println(r.getISOCode()  + " : " + r.getName());
        });

    }
}
