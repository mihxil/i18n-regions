import org.meeuw.i18n.countries.*;
import org.meeuw.i18n.regions.spi.RegionProvider;

open module org.meeuw.i18n.countries {
    exports org.meeuw.i18n.countries;
    exports org.meeuw.i18n.countries.validation;


    requires static org.checkerframework.checker.qual;
    requires static java.annotation;
    requires static java.xml.bind;
    requires transitive org.meeuw.i18n.regions;
    requires transitive nv.i18n;
    requires transitive org.meeuw.i18n.formerlyassigned;

    requires static java.validation;
    requires static org.junit.jupiter.api;
    requires static org.junit.jupiter.params;
    requires static org.assertj.core;

    uses RegionProvider;

    provides RegionProvider with

        CurrentCountryProvider,
        FormerCountryProvider,

        UserAssignedCountryProvider,
        UnofficialCurrentCountryProvider,
        VehicleRegistrationCodeFallbackProvider;

}
