import org.meeuw.i18n.spi.RegionProvider;
import org.meeuw.i18n.subdivisions.CountrySubdivisionProvider;

module org.meeuw.i18n.subdivisions {

    exports org.meeuw.i18n.subdivisions;

    requires transitive org.meeuw.i18n;

    requires static org.checkerframework.checker.qual;
    requires i18n.subdivisions;

    provides RegionProvider with

        CountrySubdivisionProvider;

}
