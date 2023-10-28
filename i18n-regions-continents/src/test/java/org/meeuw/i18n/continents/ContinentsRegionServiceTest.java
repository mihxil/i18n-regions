package org.meeuw.i18n.continents;

import java.util.Locale;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.meeuw.i18n.regions.Region;
import org.meeuw.i18n.regions.RegionService;

import com.neovisionaries.i18n.LanguageCode;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class ContinentsRegionServiceTest {

     @Test
     public void getContinent() {
         Region af = RegionService.getInstance().getByCode("CONTINENT-AF", Continent.class).orElse(null);
         assertThat(af).isNotNull();
         assertThat(af).isInstanceOf(Continent.class);
         assertThat(af.getCode()).isEqualTo("CONTINENT-AF");
         assertThat(af.toString()).isEqualTo("CONTINENT-AF");
         assertThat(af.getType()).isEqualTo(Region.Type.CONTINENT);
         assertThat(af.toLocale()).isNull();
         assertThat(af.getName()).isEqualTo("Africa");
         assertThat(af.getName(new Locale("nl"))).isEqualTo("Afrika");


         assertThat(RegionService.getInstance().getByCode("CONTINENT-XXX", Continent.class)).isNotPresent();
         assertThat(RegionService.getInstance().getByCode("XXX", Continent.class)).isNotPresent();

        assertThat(RegionService.getInstance().getProviders().toString()).isEqualTo("[ContinentProvider (7 continents)]");

    }

    @SuppressWarnings({"ConstantConditions", "EqualsWithItself"})
    @Test
    public void equalsAndHashCode() {
        Region af = RegionService.getInstance().getByCode("CONTINENT-AF", Continent.class).orElse(null);
        Region eu = RegionService.getInstance().getByCode("CONTINENT-EU", Continent.class).orElse(null);

        assertThat(af.equals(eu)).isFalse();
        assertThat(af.equals(af)).isTrue();
        assertThat(af.equals(null)).isFalse();
        assertThat(af.equals(new Object())).isFalse();
        assertThat(af.hashCode()).isEqualTo(af.hashCode());
    }
    @Test
    public void values() {
        RegionService.getInstance().values().forEach(r -> {
            System.out.println(r.getClass().getSimpleName() + ":" + r.getCode()  + " : " + r.getName() + ":" + r.getName(LanguageCode.nl));
        });

    }

    @Test
    public void valuesAsType() {
        Stream<? extends Continent> values = org.meeuw.i18n.regions.RegionService.getInstance().values(Continent.class);
        assertThat(values).hasSize(7);
    }

}
