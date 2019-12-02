import org.meeuw.i18n.regions.spi.RegionProvider;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
open module org.meeuw.i18n.test.regions {
    exports org.meeuw.i18n.test.testcountry;
    requires transitive org.meeuw.i18n.regions;

    requires java.xml.bind;
    requires java.validation;

    provides RegionProvider with

        org.meeuw.i18n.test.testcountry.TestCountryProvider;
}
