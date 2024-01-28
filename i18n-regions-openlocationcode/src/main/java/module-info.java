import org.meeuw.i18n.openlocationcode.OpenLocationProvider;
import org.meeuw.i18n.regions.spi.RegionProvider;

module org.meeuw.i18n.openlocationcode {

    exports org.meeuw.i18n.openlocationcode;
    opens org.meeuw.i18n.openlocationcode;

    requires transitive org.meeuw.i18n.regions;

    requires static org.checkerframework.checker.qual;
    requires static jakarta.validation;
    requires static jakarta.annotation;

    requires java.logging;
    requires openlocationcode;


    provides RegionProvider with

        OpenLocationProvider;

}
