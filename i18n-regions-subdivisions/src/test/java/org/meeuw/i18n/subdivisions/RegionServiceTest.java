package org.meeuw.i18n.subdivisions;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.meeuw.i18n.regions.*;

import com.neovisionaries.i18n.CountryCode;
import com.neovisionaries.i18n.LanguageCode;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class RegionServiceTest {



    @Test
    public void getCountrySubDivisionUtrecht() {

        Region utrecht = RegionService.getInstance().getByCode("NL-UT").orElse(null);
        assertThat(utrecht).isNotNull();
        assertThat(utrecht).isInstanceOf(CountrySubdivisionWithCode.class);
        assertThat(utrecht.getCode()).isEqualTo("NL-UT");
        assertThat(utrecht.getName()).isEqualTo("Utrecht");
    }

    @Test
    @Disabled("This is missing in subdivision, make pull request")
    public void getCountrySubDivisionGreatBritain() {

        Region gbn = RegionService.getInstance().getByCode("GB-GBN").orElse(null);
        assertThat(gbn).isNotNull();
        assertThat(gbn).isInstanceOf(CountrySubdivisionWithCode.class);
        assertThat(gbn.getCode()).isEqualTo("NL-UT");
        assertThat(gbn.getName()).isEqualTo("Utrecht");
    }


    @Test
    public void subdivisionsOfGB(){
        Region eng = RegionService.getInstance().getByCode("GB-ENG").orElseThrow();
        assertThat(eng.getCode()).isEqualTo("GB-ENG");
        assertThat(eng.getName()).isEqualTo("England");
        assertThat(eng.getName(LanguageCode.nl)).isEqualTo("Engeland");

        Region engOf = CountrySubdivision.of(CountryCode.GB, "ENG").orElseThrow();
        assertThat(engOf).isEqualTo(eng);

        assertThat(Regions.toString(eng, LanguageCode.nl)).isEqualTo("Engeland (GB)");
        assertThat(Regions.toStringWithCode(eng, LanguageCode.nl)).isEqualTo("GB-ENG:Engeland (GB)");

    }



}
