package org.meeuw.i18n.countries;

import java.io.IOException;
import java.net.URI;

import javax.net.ssl.HttpsURLConnection;

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
    public void properties() throws IOException {
        CurrentCountry nl = currentCountryProvider.getByCode("NL", true).orElseThrow();

        assertThat(nl.getCode()).isEqualTo(CountryCode.NL.getAlpha2());
        assertThat(nl.getAlpha2()).isEqualTo("NL");
        assertThat(nl.getAlpha3()).isEqualTo("NLD");
        assertThat(nl.getNumeric()).isEqualTo(528);
        assertThat(nl.getCountryCode()).isEqualTo(CountryCode.NL);
        {
            CurrentCountry.ALWAYS_USE_CDN_FOR_ICONS.set(false);
            URI icon = nl.getIcon().orElse(null);
            assertThat(icon).isNotNull();
            assertThat(getClass().getClassLoader().getResourceAsStream("META-INF/resources" + icon.toString())).isNotNull();
        }
        {
            CurrentCountry.ALWAYS_USE_CDN_FOR_ICONS.set(true);
            URI icon = nl.getIcon().orElse(null);
            HttpsURLConnection con = (HttpsURLConnection) icon.toURL().openConnection();
            assertThat(con.getResponseCode()).isEqualTo(200);
        }

    }

    @SuppressWarnings({"ConstantConditions", "EqualsWithItself"})
    @Test
    public void equalsAndHashCode() {
        Region nl = currentCountryProvider.getByCode("NL").orElseThrow();
        Region be = currentCountryProvider.getByCodeOrNull("BE");

        assertThat(nl.equals(be)).isFalse();
        assertThat(nl.equals(nl)).isTrue();
        assertThat(nl.equals(null)).isFalse();
        assertThat(nl.equals(new Object())).isFalse();
        assertThat(nl.hashCode()).isEqualTo(nl.hashCode());
    }

}
