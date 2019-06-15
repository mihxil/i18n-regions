package org.meeuw.i18n;

import javax.annotation.Nonnull;
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
	public boolean canProvide(@Nonnull Class<? extends Region> clazz) {
		return clazz.isAssignableFrom(CurrentCountry.class);

	}

	@Override
	public Optional<CurrentCountry> getByCode(@Nonnull String code) {
		try {
			VehicleRegistrationCode vehicleRegistrationCode = VehicleRegistrationCode.valueOf(code);
			return Optional.of(new CurrentCountry(vehicleRegistrationCode.getCode()));
		} catch (IllegalArgumentException iae) {
			return Optional.empty();
		}

	}

	/**
	 * Returns an empty stream. All values should be provided by {@link CurrentCountryProvider}.
	 */
	@Override
	public Stream<CurrentCountry> values() {
		return Stream.empty();

	}
}
