import org.meeuw.i18n.regions.spi.RegionProvider;
import org.meeuw.i18n.subdivisions.CountrySubdivisionProvider;
import org.meeuw.i18n.subdivisions.UserAssignedCountrySubdivisionProvider;


module org.meeuw.i18n.subdivisions {

    exports org.meeuw.i18n.subdivisions;
    exports org.meeuw.i18n.subdivisions.validation;
    exports org.meeuw.i18n.subdivisions.validation.impl; // to hibernate validation

    requires transitive org.meeuw.i18n.regions;

    requires static org.checkerframework.checker.qual;

    requires org.meeuw.i18n.subdivision_enums;
    requires jakarta.validation;
    requires java.logging;
    requires org.meeuw.i18n.countries;

    provides RegionProvider with

        CountrySubdivisionProvider,
        UserAssignedCountrySubdivisionProvider;


}
