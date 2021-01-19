package org.meeuw.i18n.countries;

import org.junit.jupiter.api.Test;
import org.meeuw.i18n.regions.Region;

import com.neovisionaries.i18n.CountryCode;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author Michiel Meeuwissen
 * @since 0.2
 */
public class CurrentCountryProviderTest {

    private final CurrentCountryProvider currentCountryProvider = new CurrentCountryProvider();

    @Test
    public void canProvide() {
        assertThat(currentCountryProvider.canProvide(CurrentCountry.class)).isTrue();
        assertThat(currentCountryProvider.canProvide(FormerCountry.class)).isFalse();
        assertThat(currentCountryProvider.canProvide(Region.class)).isTrue();
    }

    @Test
    public void properties() {
        CurrentCountry nl = currentCountryProvider.getByCode("NL", true).orElseThrow();

        assertThat(nl.getCode()).isEqualTo(CountryCode.NL.getAlpha2());
        assertThat(nl.getAlpha2()).isEqualTo("NL");
        assertThat(nl.getAlpha3()).isEqualTo("NLD");
        assertThat(nl.getNumeric()).isEqualTo(528);
        assertThat(nl.getCountryCode()).isEqualTo(CountryCode.NL);
    }

    @SuppressWarnings({"ConstantConditions", "EqualsWithItself"})
    @Test
    public void equalsAndHashCode() {
        Region nl = currentCountryProvider.getByCode("NL").orElseThrow();
        Region be = currentCountryProvider.getByCode("BE").orElseThrow();;

        assertThat(nl.equals(be)).isFalse();
        assertThat(nl.equals(nl)).isTrue();
        assertThat(nl.equals(null)).isFalse();
        assertThat(nl.equals(new Object())).isFalse();
        assertThat(nl.hashCode()).isEqualTo(nl.hashCode());
    }

}
