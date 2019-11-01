import org.meeuw.i18n.regions.spi.RegionProvider;
import org.meeuw.i18n.subdivisions.CountrySubdivisionProvider;

module org.meeuw.i18n.subdivisions {

    exports org.meeuw.i18n.subdivisions;

    requires transitive org.meeuw.i18n.regions;

    requires static org.checkerframework.checker.qual;
    requires i18n.subdivisions;
    requires java.validation;

    provides RegionProvider with

        CountrySubdivisionProvider;

}
