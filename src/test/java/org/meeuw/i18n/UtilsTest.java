package org.meeuw.i18n;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author Michiel Meeuwissen
 * @since ...
 */
public class UtilsTest {

    @Test
    public void getCurrentByCode() {

        Region nl = Utils.getByCode("NL");
        assertThat(nl).isNotNull();
        assertThat(nl).isInstanceOf(CurrentCountry.class);
        assertThat(nl.getISOCode()).isEqualTo("NL");
        assertThat(nl.getName()).isEqualTo("Netherlands");

    }
    @Test
    public void getFormerByCode() {

        Region cshh = Utils.getByCode("CSHH");
        assertThat(cshh).isNotNull();
        assertThat(cshh).isInstanceOf(FormerCountry.class);
        assertThat(cshh.getISOCode()).isEqualTo("CSHH");
        assertThat(cshh.getName()).isEqualTo("Czechoslovakia");
    }

    @Test
    public void getCountrySubDivision() {

        Region utrecht = Utils.getByCode("NL:UT");
        assertThat(utrecht).isNotNull();
        assertThat(utrecht).isInstanceOf(CountrySubDivision.class);
        assertThat(utrecht.getISOCode()).isEqualTo("NL:UT");
        assertThat(utrecht.getName()).isEqualTo("Utrecht");
    }


    @Test
    public void values() {

        Utils.values().forEach(r -> {
            System.out.println(r.getISOCode()  + " : " + r.getName());
        });

    }
}
