package org.meeuw.i18n.countries;

import java.net.URI;
import java.util.*;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.meeuw.i18n.regions.Region;
import org.meeuw.i18n.regions.UserAssignedRegion;

import com.neovisionaries.i18n.CountryCode;
import com.neovisionaries.i18n.LanguageCode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.i18n.regions.RegionService.getInstance;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class RegionServiceTest {

    @Test
    public void getCurrentByCode() {
        CurrentCountry netherlands = org.meeuw.i18n.regions.RegionService.getInstance().getByCode("NL", CurrentCountry.class).orElseThrow();
        CountryCode countryCode = netherlands.getCountryCode();
        Optional<URI> icon = netherlands.getIcon();
        String nameInLocalLanguage = netherlands.getLocalName();



        Optional<Country> nl = getInstance().getByCode("NL", Country.class);
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
        Optional<CurrentCountry> nl = getInstance().getByCode("NL", CurrentCountry.class);
        assertThat(nl.get().getAlpha3()).isEqualTo("NLD");
    }
    @Test
    public void getFormerByCode() {
        Region cshh = getInstance().getByCode("CSHH").orElse(null);
        assertThat(cshh).isNotNull();
        assertThat(cshh).isInstanceOf(FormerCountry.class);
        assertThat(cshh.getCode()).isEqualTo("CSHH");
        assertThat(cshh.getName()).isEqualTo("Czechoslovakia");
        assertThat(cshh.getName(new Locale("nl"))).isEqualTo("Tsjechoslowakije");

    }
    @Test
    public void getFormerByCodeAsCountry() {
        Optional<Country> cshh = getInstance().getByCode("CSHH", Country.class);
        assertThat(cshh.get().getCode()).isEqualTo("CSHH");
        assertThat(((FormerCountry) cshh.get()).getFormerCodes().get(0)).isEqualTo("CS");
    }


    @Test
    public void getCountryZZ() {

        Region undefined = getInstance().getByCode("ZZ").orElse(null);
        assertThat(undefined).isNotNull();
        assertThat(undefined).isInstanceOf(UserAssignedRegion.class);
        assertThat(undefined.getCode()).isEqualTo("ZZ");
        assertThat(undefined.getName()).isEqualTo("Unknown or Invalid Territory");
        assertThat(undefined.getName(new Locale("nl"))).isEqualTo("Onbekend of ongeldig gebied");

    }


    @Test
    public void getFormerCountryCS() {

        FormerCountry cshh = getInstance().getByCode("CS", FormerCountry.class).orElse(null);
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
        Region tptl = getInstance().getByCode("TPTL").orElse(null);
        assertThat(tptl).isNotNull();
        assertThat(tptl).isInstanceOf(FormerCountry.class);
        assertThat(tptl.getCode()).isEqualTo("TPTL");
        assertThat(tptl.getName()).isEqualTo("East Timor");
        assertThat(tptl.getName(new Locale("nl"))).isEqualTo("Oost Timor");
    }

    @Test
    public void getSaintHelena() {
        Region sh = getInstance().getByCode("sh").orElse(null);
        assertThat(sh).isNotNull();
        assertThat(sh).isInstanceOf(CurrentCountry.class);
    }


    @Test
    public void stream() {
        Stream<? extends Region> values = getInstance().values(Region.Type.COUNTRY);

        Spliterator<? extends Region> spliterator = values.spliterator();
        assertThat(spliterator.estimateSize()).isEqualTo(Long.MAX_VALUE);
        Spliterator<? extends Region> split = spliterator.trySplit();
        assertThat(split).isNull();
        spliterator.forEachRemaining(r -> {
            System.out.println(r.toString());
        });
    }

    @Test
    public void values() {
        CurrentCountry.ALWAYS_USE_CDN_FOR_ICONS.set(true);
        getInstance().values().forEach(r -> {
            StringBuilder build = new StringBuilder();
            r.toStringBuilder(build, LanguageCode.nl.toLocale());
            System.out.println(
                r.getClass().getSimpleName() + ":" + r.getCode()  + " : " + r.getName() + ":" + r.getName(LanguageCode.nl) + ":" + r.getIcon().map(URI::toString).orElse("none") + ": " + build);
        });

    }

}
