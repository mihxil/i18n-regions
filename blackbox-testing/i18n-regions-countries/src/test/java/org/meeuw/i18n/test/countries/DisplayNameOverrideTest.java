package org.meeuw.i18n.test.countries;

import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.meeuw.i18n.countries.Country;
import org.meeuw.i18n.countries.CurrentCountry;
import org.meeuw.i18n.countries.codes.CountryCode;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests whether the display names of countries can be overridden by providing {@code Country_<locale>.properties} resource bundles.
 * <p>
 * Languages not supported by i18n-regions-countries itself (like Frisian) can be added directly. To override names in a supported language, use a locale with a variant (e.g. {@code Country_nl_NL_custom.properties}), which is more specific, and falls back per key to the shipped bundle.
 */
class DisplayNameOverrideTest {

    static final Locale DUTCH = new Locale("nl");
    static final Locale DUTCH_CUSTOM = new Locale("nl", "NL", "custom");
    static final Locale FRISIAN = new Locale("fy");
    static final Locale FLEMISH = new Locale("nl", "BE");

    @Test
    void dutch() {
        // Country_nl is supplied by i18n-regions-countries itself, and can't be reliably overridden. Use a variant for that.
        assertThat(CurrentCountry.of(CountryCode.NL).getName(DUTCH)).isEqualTo("Nederland");
        assertThat(CurrentCountry.of(CountryCode.NL).getName(DUTCH_CUSTOM)).isEqualTo("Holland");
        // other entries of the Country_nl bundle shipped with i18n-regions-countries should still be available
        assertThat(Country.getByCode("CSHH").orElseThrow().getName(DUTCH_CUSTOM)).isEqualTo("Tsjechoslowakije");
        // not overridden at all, falls back to the JDK
        assertThat(CurrentCountry.of(CountryCode.DE).getName(DUTCH_CUSTOM)).isEqualTo("Duitsland");
    }

    @Test
    void frisian() {
        assertThat(CurrentCountry.of(CountryCode.NL).getName(FRISIAN)).isEqualTo("Nederlân");
        assertThat(CurrentCountry.of(CountryCode.DE).getName(FRISIAN)).isEqualTo("Dútslân");
    }

    @Test
    void variant() {
        assertThat(CurrentCountry.of(CountryCode.NL).getName(FLEMISH)).isEqualTo("Nederland (gezien vanuit België)");
        // falls back to the 'nl' bundle
        assertThat(Country.getByCode("CSHH").orElseThrow().getName(FLEMISH)).isEqualTo("Tsjechoslowakije");
        assertThat(CurrentCountry.of(CountryCode.DE).getName(FLEMISH)).isEqualTo("Duitsland");
    }
}
