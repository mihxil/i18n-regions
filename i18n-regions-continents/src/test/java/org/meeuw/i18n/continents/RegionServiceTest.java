package org.meeuw.i18n.continents;

import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.meeuw.i18n.regions.Region;
import org.meeuw.i18n.regions.RegionService;

import com.neovisionaries.i18n.LanguageCode;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class RegionServiceTest {

     @Test
     public void getContinent() {
        Region af = RegionService.getInstance().getByCode("CONTINENT-AF", Continent.class).orElse(null);
        assertThat(af).isNotNull();
        assertThat(af).isInstanceOf(Continent.class);
        assertThat(af.getCode()).isEqualTo("CONTINENT-AF");
        assertThat(af.getName()).isEqualTo("Africa");
        assertThat(af.getName(new Locale("nl"))).isEqualTo("Afrika");

    }
    @Test
    public void values() {

        RegionService.getInstance().values().forEach(r -> {
            System.out.println(r.getClass().getSimpleName() + ":" + r.getCode()  + " : " + r.getName() + ":" + r.getName(LanguageCode.nl));
        });

    }

}
