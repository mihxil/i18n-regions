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

        Optional<Country> nl = Regions.getByCode("NL", Country.class);
        assertThat(nl).isPresent();
        assertThat(nl.get()).isInstanceOf(CurrentCountry.class);
        assertThat(nl.get().getCode()).isEqualTo("NL");
        assertThat(nl.get().getName()).isEqualTo("Netherlands");
        assertThat(nl.get().getName(Locale.GERMAN)).isEqualTo("Niederlande");
        assertThat(nl.get().toLocale()).isEqualTo(new Locale("", "NL"));
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
        assertThat(cshh.getCode()).isEqualTo("CSHH");
        assertThat(cshh.getName()).isEqualTo("Czechoslovakia");
        assertThat(cshh.getName(new Locale("nl"))).isEqualTo("Tsjechoslowakije");

    }
    @Test
    public void getFormerByCodeAsCountry() {
        Optional<Country> nl = Regions.getByCode("CSHH", Country.class);
        assertThat(nl.get().getCode()).isEqualTo("CSHH");
        assertThat(nl.get().getAlpha2()).isEqualTo("CS");
    }

    @Test
    public void getCountrySubDivisionUtrecht() {

        Region utrecht = Regions.getByCode("NL-UT").orElse(null);
        assertThat(utrecht).isNotNull();
        assertThat(utrecht).isInstanceOf(CountrySubDivision.class);
        assertThat(utrecht.getCode()).isEqualTo("NL-UT");
        assertThat(utrecht.getName()).isEqualTo("Utrecht");
    }

    @Test
    @Ignore("This is missing in subdivision, make pull request")
    public void getCountrySubDivisionGreatBritain() {

        Region gbn = Regions.getByCode("GB-GBN").orElse(null);
        assertThat(gbn).isNotNull();
        assertThat(gbn).isInstanceOf(CountrySubDivision.class);
        assertThat(gbn.getCode()).isEqualTo("NL-UT");
        assertThat(gbn.getName()).isEqualTo("Utrecht");
    }


    @Test
    public void getCountryZZ() {

        Region undefined = Regions.getByCode("ZZ").orElse(null);
        assertThat(undefined).isNotNull();
        assertThat(undefined).isInstanceOf(UserAssignedCountry.class);
        assertThat(undefined.getCode()).isEqualTo("ZZ");
        assertThat(undefined.getName()).isEqualTo("Unknown or Invalid Territory");
        assertThat(undefined.getName(new Locale("nl"))).isEqualTo("Onbekend of ongeldig gebied");

    }


    @Test
    public void getFormerCountryCS() {

        Region cshh = Regions.getByCode("CS", FormerCountry.class).orElse(null);
        assertThat(cshh).isNotNull();
        assertThat(cshh).isInstanceOf(FormerCountry.class);
        // It should find the country most recently assigned to 'CS'.
        // That's Serbia and Montenegro, not Czechoslovakia.
        assertThat(cshh.getCode()).isEqualTo("CSXX");
        assertThat(cshh.getName()).isEqualTo("Serbia and Montenegro");
    }

    @Test
    public void values() {

        Regions.values().forEach(r -> {
            System.out.println(r.getCode()  + " : " + r.getName());
        });

    }
}