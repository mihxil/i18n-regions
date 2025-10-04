package org.meeuw.i18n.subdivisions;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.meeuw.i18n.countries.CurrentCountry;
import org.meeuw.i18n.countries.codes.CountryCode;
import org.meeuw.i18n.regions.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.i18n.languages.ISO_639_1_Code.nl;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */

public class SubDivisionsRegionServiceTest {

    @Test
    public void getCountrySubDivisionUtrecht() {

        Region utrecht = RegionService.getInstance().getByCode("NL-UT").orElse(null);
        assertThat(utrecht).isNotNull();
        assertThat(utrecht).isInstanceOf(CountrySubdivisionWithCode.class);
        assertThat(utrecht.getCode()).isEqualTo("NL-UT");
        assertThat(utrecht.getName()).isEqualTo("Utrecht");
    }

    @Test
    public void getCountrySubDivisionGreatBritain() {

        Region gbn = RegionService.getInstance().getByCode("GB-GBN").orElse(null);
        assertThat(gbn).isNotNull();
        assertThat(gbn).isInstanceOf(CountrySubdivisionWithCode.class);
        assertThat(gbn.getCode()).isEqualTo("GB-GBN");
        assertThat(gbn.getName()).isEqualTo("Great Britain");
    }


    @Test
    public void subdivisionsOfGB(){
        Region eng = RegionService.getInstance().getByCode("GB-ENG").orElseThrow();
        assertThat(eng.getCode()).isEqualTo("GB-ENG");
        assertThat(eng.getName()).isEqualTo("England");
        assertThat(eng.getName(nl)).isEqualTo("Engeland");

        RegionService.getInstance().values().filter(r -> r.getCode().startsWith("GB-")).forEach(gb -> {
            System.out.println(gb + ":" + gb.getLocalName());
        });

        Region engOf = CountrySubdivision.of(CurrentCountry.of(CountryCode.GB), "ENG").orElseThrow();
        assertThat(engOf).isEqualTo(eng);

        assertThat(Regions.toString(eng, nl)).isEqualTo("Engeland (Verenigd Koninkrijk)");
        assertThat(Regions.toStringWithCode(eng, nl)).isEqualTo("GB-ENG:Engeland (Verenigd Koninkrijk)");


    }

    @Test
    public void getCode() {
        Optional<Region> byCode = RegionService.getInstance().getByCode("NL-DR");
        assertThat(byCode).isPresent();
        assertThat(byCode).containsInstanceOf(CountrySubdivisionWithCode.class);
        assertThat(((CountrySubdivisionWithCode) byCode.get()).getCountrySubdivisionCode().name()).isEqualTo("DR");
    }

    @Test
    public void getLocale() {
        Optional<Region> byCode = RegionService.getInstance().getByCode("NL-DR");
        assertThat(byCode).containsInstanceOf(CountrySubdivisionWithCode.class);
        assertThat(byCode.get().toLocale().getCountry()).isEqualTo("NL");
    }


}
