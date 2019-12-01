import org.meeuw.i18n.regions.spi.RegionProvider;
import org.meeuw.i18n.subdivisions.CountrySubdivisionProvider;
import org.meeuw.i18n.subdivisions.UserAssignedCountrySubdivisionProvider;


module org.meeuw.i18n.subdivisions {

    exports org.meeuw.i18n.subdivisions;
    exports org.meeuw.i18n.subdivisions.validation;
    exports org.meeuw.i18n.subdivisions.validation.impl; // to hibernate validation


    requires transitive org.meeuw.i18n.regions;

    requires static org.checkerframework.checker.qual;
    requires i18n.subdivisions;
    requires java.validation;
    requires java.logging;

    provides RegionProvider with

        CountrySubdivisionProvider,
        UserAssignedCountrySubdivisionProvider;


}
