package org.meeuw.i18n.subdivisions;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.meeuw.i18n.regions.Region;
import org.meeuw.i18n.regions.RegionService;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
class RegionServiceTest {



    @Test
    void getCountrySubDivisionUtrecht() {

        Region utrecht = RegionService.getInstance().getByCode("NL-UT").orElse(null);
        assertThat(utrecht).isNotNull();
        assertThat(utrecht).isInstanceOf(CountrySubdivisionWithCode.class);
        assertThat(utrecht.getCode()).isEqualTo("NL-UT");
        assertThat(utrecht.getName()).isEqualTo("Utrecht");
    }

    @Test
    @Disabled("This is missing in subdivision, make pull request")
    void getCountrySubDivisionGreatBritain() {

        Region gbn = RegionService.getInstance().getByCode("GB-GBN").orElse(null);
        assertThat(gbn).isNotNull();
        assertThat(gbn).isInstanceOf(CountrySubdivisionWithCode.class);
        assertThat(gbn.getCode()).isEqualTo("NL-UT");
        assertThat(gbn.getName()).isEqualTo("Utrecht");
    }


    @Test
    void subdivisionsOfGB(){
        Region eng = RegionService.getInstance().getByCode("GB-ENG").orElse(null);
        assertThat(eng.getCode()).isEqualTo("GB-ENG");
        assertThat(eng.getName()).isEqualTo("England");
    }



}
