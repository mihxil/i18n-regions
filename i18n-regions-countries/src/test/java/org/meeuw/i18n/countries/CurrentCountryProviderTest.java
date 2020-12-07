package org.meeuw.i18n.countries;

import org.junit.jupiter.api.Test;
import org.meeuw.i18n.regions.Region;

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

}
