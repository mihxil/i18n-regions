import org.meeuw.i18n.regions.spi.RegionProvider;
import org.meeuw.i18n.test.some.SomeRegionProvider;

/**
 * @author Michiel Meeuwissen
 */
open module org.meeuw.i18n.test.countries {
    requires transitive org.meeuw.i18n.countries;
    requires org.meeuw.i18n.languages;

    requires jakarta.validation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;

    uses RegionProvider;

    provides RegionProvider with

         SomeRegionProvider;

}
