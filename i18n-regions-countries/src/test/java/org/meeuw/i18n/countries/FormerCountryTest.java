package org.meeuw.i18n.countries;

import java.time.Year;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
class FormerCountryTest {

	@Test
    void getValidity() {
		new FormerCountryProvider().values().forEach((country) -> {
			// it wouldn't be former otherwise!
			assertThat(country.getValidity().contains(Year.now())).isFalse();
		});
	}
}
