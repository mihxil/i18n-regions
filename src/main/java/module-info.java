import org.meeuw.i18n.continents.ContinentProvider;
import org.meeuw.i18n.countries.*;
import org.meeuw.i18n.spi.RegionProvider;
import org.meeuw.i18n.subdivisions.CountrySubdivisionProvider;
import org.meeuw.i18n.subdivisions.UserAssignedCountrySubdivisionProvider;

module org.meeuw.i18n {
    exports org.meeuw.i18n;
    exports org.meeuw.i18n.bind.jaxb;
    exports org.meeuw.i18n.continents;
    exports org.meeuw.i18n.countries;
    exports org.meeuw.i18n.persistence;
    exports org.meeuw.i18n.subdivisions;
    exports org.meeuw.i18n.validation;
    exports org.meeuw.i18n.spi;

    requires transitive nv.i18n;
    requires transitive i18n.subdivisions;
    requires transitive org.meeuw.i18n.formerlyassigned;

    requires static java.validation;
    requires static java.xml.bind;
    requires static org.checkerframework.checker.qual;
    requires static java.persistence;

    provides RegionProvider with

        CurrentCountryProvider,
        FormerCountryProvider,

        UserAssignedProvider,
        UserAssignedCountrySubdivisionProvider,
        UnofficialCurrentCountryProvider,
        VehicleRegistrationCodeFallbackProvider,

        CountrySubdivisionProvider,

        ContinentProvider;

}
