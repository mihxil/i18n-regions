import org.meeuw.i18n.test.some.SomeRegionProvider;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
open module org.meeuw.i18n.test.countries {
    requires org.junit.jupiter.api;
    requires org.meeuw.i18n.countries;
    requires org.assertj.core;
    requires java.validation;

    exports org.meeuw.i18n.test.some;


    provides org.meeuw.i18n.regions.spi.RegionProvider with

         SomeRegionProvider;

}
