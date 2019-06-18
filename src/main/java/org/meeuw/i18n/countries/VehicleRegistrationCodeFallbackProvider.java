package org.meeuw.i18n.countries;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.i18n.Region;
import org.meeuw.i18n.spi.RegionProvider;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * This provider can only 'getByCode', and serves as a fall back for countries. As a fallback, if no region with a given code could be found, this will match on the {@link VehicleRegistrationCode}.
 *
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class VehicleRegistrationCodeFallbackProvider implements RegionProvider<CurrentCountry> {

	@Override
	public boolean canProvide(@NonNull Class<? extends Region> clazz) {
		return clazz.isAssignableFrom(CurrentCountry.class);

	}

	@Override
	public Optional<CurrentCountry> getByCode(@NonNull String code) {
		try {
			VehicleRegistrationCode vehicleRegistrationCode = VehicleRegistrationCode.valueOf(code);
			return Optional.of(new CurrentCountry(vehicleRegistrationCode.getCode()));
		} catch (IllegalArgumentException iae) {
			return Optional.empty();
		}

	}

	/**
	 * Returns an empty stream. All values should be provided by {@link AbstractCurrentCountryProvider}.
	 */
	@Override
	public Stream<CurrentCountry> values() {
		return Stream.empty();

	}
}
