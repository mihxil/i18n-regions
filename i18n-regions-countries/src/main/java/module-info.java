import org.meeuw.i18n.countries.*;
import org.meeuw.i18n.regions.spi.RegionProvider;

module org.meeuw.i18n.countries {
    exports org.meeuw.i18n.countries;
    exports org.meeuw.i18n.countries.validation;
    exports org.meeuw.i18n.countries.validation.impl; // to hibernate validation

    requires static org.checkerframework.checker.qual;
    requires static jakarta.annotation;
    requires static jakarta.xml.bind;
    requires static jakarta.validation;
    requires static jakarta.persistence;


    requires transitive org.meeuw.i18n.regions;
    requires transitive nv.i18n;
    requires transitive org.meeuw.i18n.languages;
    requires transitive org.meeuw.i18n.formerlyassigned;



    requires java.logging;
    requires static com.fasterxml.jackson.annotation;

    uses RegionProvider;

    provides RegionProvider with

        CurrentCountryProvider,
        FormerCountryProvider,

        UserAssignedCountryProvider,
        UnofficialCurrentCountryProvider,
        VehicleRegistrationCodeFallbackProvider;

}
