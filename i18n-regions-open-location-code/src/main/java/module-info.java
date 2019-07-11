import org.meeuw.i18n.openlocationcode.OpenLocationProvider;
import org.meeuw.i18n.spi.RegionProvider;

module org.meeuw.i18n.continents {

    exports org.meeuw.i18n.openlocationcode;

    requires transitive org.meeuw.i18n;

    requires static org.checkerframework.checker.qual;
    requires static java.validation;
    requires static java.annotation;

    requires openlocationcode;


    provides RegionProvider with

        OpenLocationProvider;

}
