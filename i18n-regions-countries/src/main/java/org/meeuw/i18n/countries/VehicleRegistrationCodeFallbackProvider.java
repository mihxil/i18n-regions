package org.meeuw.i18n.countries;

import java.util.Optional;
import java.util.stream.Stream;

import javax.annotation.Priority;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.i18n.spi.RegionProvider;

/**
 * This provider can only 'getByCode', and serves as a fall back for countries. As a fallback, if no region with a given code could be found, this will match on the {@link VehicleRegistrationCode}.
 *
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Priority(100)
public class VehicleRegistrationCodeFallbackProvider implements RegionProvider<CurrentCountry> {



	@Override
	public Optional<CurrentCountry> getByCode(@NonNull String code, boolean lenient) {
		if (! lenient) {
			return Optional.empty();
		} else {
			code = code.toUpperCase();
		}
		try {
			VehicleRegistrationCode vehicleRegistrationCode = VehicleRegistrationCode.valueOf(code);
			return Optional.of(new CurrentCountry(vehicleRegistrationCode.getCode()));
		} catch (IllegalArgumentException iae) {
			return Optional.empty();
		}

	}

	@Override
	public Class<CurrentCountry> getProvidedClass() {
		return CurrentCountry.class;
	}

	/**
	 * Returns an empty stream. All values should be provided by {@link AbstractCurrentCountryProvider}.
	 */
	@Override
	public Stream<CurrentCountry> values() {
		return Stream.empty();

	}
}
