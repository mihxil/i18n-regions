package org.meeuw.i18n.countries;

import java.util.Locale;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.meeuw.i18n.Region;
import org.meeuw.i18n.RegionService;
import org.meeuw.i18n.UserAssignedRegion;

import com.neovisionaries.i18n.CountryCode;
import com.neovisionaries.i18n.LanguageCode;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class RegionServiceTest {

    @Test
    public void getCurrentByCode() {

        Optional<Country> nl = RegionService.getInstance().getByCode("NL", Country.class);
        Assertions.assertThat(nl).isPresent();
        assertThat(nl.get()).isInstanceOf(CurrentCountry.class);
        assertThat(nl.get().getCode()).isEqualTo("NL");
        assertThat(nl.get().getName()).isEqualTo("Netherlands");
        assertThat(nl.get().getName(Locale.GERMAN)).isEqualTo("Niederlande");
        assertThat(nl.get().toLocale()).isEqualTo(new Locale("", "NL"));
        assertThat(nl.get().getName(new Locale("nl"))).isEqualTo("Nederland");
    }

    @Test
    public void getCurrentByCodeAsCountry() {
        Optional<CurrentCountry> nl = RegionService.getInstance().getByCode("NL", CurrentCountry.class);
        assertThat(nl.get().getAlpha3()).isEqualTo("NLD");
    }
    @Test
    public void getFormerByCode() {
        Region cshh = RegionService.getInstance().getByCode("CSHH").orElse(null);
        assertThat(cshh).isNotNull();
        assertThat(cshh).isInstanceOf(FormerCountry.class);
        assertThat(cshh.getCode()).isEqualTo("CSHH");
        assertThat(cshh.getName()).isEqualTo("Czechoslovakia");
        assertThat(cshh.getName(new Locale("nl"))).isEqualTo("Tsjechoslowakije");

    }
    @Test
    public void getFormerByCodeAsCountry() {
        Optional<Country> cshh = RegionService.getInstance().getByCode("CSHH", Country.class);
        assertThat(cshh.get().getCode()).isEqualTo("CSHH");
        assertThat(((FormerCountry) cshh.get()).getFormerCodes().get(0)).isEqualTo("CS");
    }


    @Test
    public void getCountryZZ() {

        Region undefined = RegionService.getInstance().getByCode("ZZ").orElse(null);
        assertThat(undefined).isNotNull();
        assertThat(undefined).isInstanceOf(UserAssignedRegion.class);
        assertThat(undefined.getCode()).isEqualTo("ZZ");
        assertThat(undefined.getName()).isEqualTo("Unknown or Invalid Territory");
        assertThat(undefined.getName(new Locale("nl"))).isEqualTo("Onbekend of ongeldig gebied");

    }


    @Test
    public void getFormerCountryCS() {

        Region cshh = RegionService.getInstance().getByCode("CS", FormerCountry.class).orElse(null);
        assertThat(cshh).isNotNull();
        assertThat(cshh).isInstanceOf(FormerCountry.class);
        // It should find the country most recently assigned to 'CS'.
        // That's Serbia and Montenegro, not Czechoslovakia.
        assertThat(cshh.getCode()).isEqualTo("CSXX");
        assertThat(cshh.getName()).isEqualTo("Serbia and Montenegro");
    }

    @Test
    public void byVehicleRegistration() {
        Country country = Country.getByCode("WAN").orElse(null);
        assertThat(country).isEqualTo(new CurrentCountry(CountryCode.NG));
    }

    @Test
    public void getEastTimor() {
        Region tptl = RegionService.getInstance().getByCode("TPTL").orElse(null);
        assertThat(tptl).isNotNull();
        assertThat(tptl).isInstanceOf(FormerCountry.class);
        assertThat(tptl.getCode()).isEqualTo("TPTL");
        assertThat(tptl.getName()).isEqualTo("East Timor");
        assertThat(tptl.getName(new Locale("nl"))).isEqualTo("Oost Timor");

    }

    @Test
    public void values() {

        RegionService.getInstance().values().forEach(r -> {
            System.out.println(r.getClass().getSimpleName() + ":" + r.getCode()  + " : " + r.getName() + ":" + r.getName(LanguageCode.nl));
        });

    }

}
