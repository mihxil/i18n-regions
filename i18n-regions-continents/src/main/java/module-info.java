import org.meeuw.i18n.continents.ContinentProvider;
import org.meeuw.i18n.regions.spi.RegionProvider;

module org.meeuw.i18n.continents {

    exports org.meeuw.i18n.continents;

    requires transitive org.meeuw.i18n.regions;

    requires static org.checkerframework.checker.qual;
    requires static java.annotation;

    provides RegionProvider with

        ContinentProvider;

}
