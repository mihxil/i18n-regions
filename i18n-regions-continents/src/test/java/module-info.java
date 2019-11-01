import org.meeuw.i18n.continents.ContinentProvider;
import org.meeuw.i18n.regions.spi.RegionProvider;

open module org.meeuw.i18n.continents {

    exports org.meeuw.i18n.continents;

    requires transitive org.meeuw.i18n.regions;

    requires static org.checkerframework.checker.qual;
    requires static java.annotation;

    requires static org.junit.jupiter.api;
    requires static org.junit.jupiter.params;
    requires static org.assertj.core;


    provides RegionProvider with

        ContinentProvider;

}
