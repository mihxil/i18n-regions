import org.meeuw.i18n.continents.ContinentProvider;
import org.meeuw.i18n.regions.spi.RegionProvider;

/**
 * Provides the {@link org.meeuw.i18n.regions.spi.RegionProvider} for the continents of the world
 */

module org.meeuw.i18n.continents {

    exports org.meeuw.i18n.continents;

    requires transitive org.meeuw.i18n.regions;

    requires static org.checkerframework.checker.qual;
    requires static jakarta.annotation;

    provides RegionProvider with
        ContinentProvider;

}
