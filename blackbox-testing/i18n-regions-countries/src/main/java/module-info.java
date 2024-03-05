import org.meeuw.i18n.test.some.SomeRegionProvider;

/**
 * @author Michiel Meeuwissen
 */
open module org.meeuw.i18n.test.countries {
    requires org.meeuw.i18n.countries;
    requires java.validation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;

    exports org.meeuw.i18n.test.some;


    provides org.meeuw.i18n.regions.spi.RegionProvider with

         SomeRegionProvider;

}
