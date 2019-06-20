package org.meeuw.i18n.subdivisions;

import org.junit.Ignore;
import org.junit.Test;
import org.meeuw.i18n.Region;
import org.meeuw.i18n.RegionService;

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
    @Ignore("This is missing in subdivision, make pull request")
    public void getCountrySubDivisionGreatBritain() {

        Region gbn = RegionService.getInstance().getByCode("GB-GBN").orElse(null);
        assertThat(gbn).isNotNull();
        assertThat(gbn).isInstanceOf(CountrySubdivisionWithCode.class);
        assertThat(gbn.getCode()).isEqualTo("NL-UT");
        assertThat(gbn.getName()).isEqualTo("Utrecht");
    }


}
