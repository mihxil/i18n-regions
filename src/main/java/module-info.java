import org.meeuw.i18n.*;
import org.meeuw.i18n.continents.ContinentProvider;
import org.meeuw.i18n.countries.CurrentCountryProvider;
import org.meeuw.i18n.countries.FormerCountryProvider;
import org.meeuw.i18n.countries.UnofficialCurrentCountryProvider;
import org.meeuw.i18n.countries.VehicleRegistrationCodeFallbackProvider;
import org.meeuw.i18n.subdivisions.CountrySubdivisionProvider;
import org.meeuw.i18n.subdivisions.UserAssignedCountrySubdivisionProvider;

module org.meeuw.i18n {
	exports org.meeuw.i18n;


	requires nv.i18n;
	requires i18n.subdivisions;
	requires org.meeuw.i18n.formerlyassigned;

	requires static java.validation;
    requires static java.xml.bind;
	requires static org.checkerframework.checker.qual;
	requires static java.persistence;

	provides RegionProvider with

		CurrentCountryProvider,
		FormerCountryProvider,

		org.meeuw.i18n.UserAssignedProvider,
		UserAssignedCountrySubdivisionProvider,
		UnofficialCurrentCountryProvider,
		VehicleRegistrationCodeFallbackProvider,

		CountrySubdivisionProvider,

		ContinentProvider;

}
