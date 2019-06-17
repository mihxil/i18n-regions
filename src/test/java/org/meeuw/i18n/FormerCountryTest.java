package org.meeuw.i18n;

import org.junit.Test;
import org.meeuw.i18n.countries.FormerCountryProvider;

import java.time.Year;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class FormerCountryTest {

	@Test
	public void getValidity() {
		new FormerCountryProvider().values().forEach((country) -> {
			// it wouldn't be former otherwise!
			assertThat(country.getValidity().contains(Year.now())).isFalse();
		});
	}
}
