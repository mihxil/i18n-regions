import org.meeuw.i18n.test.some.SomeRegionProvider;

/**
 * @author Michiel Meeuwissen
 */
open module org.meeuw.i18n.test.countries {
    requires org.meeuw.i18n.countries;
    requires org.meeuw.i18n.languages;
    requires jakarta.validation;



    provides org.meeuw.i18n.regions.spi.RegionProvider with

         SomeRegionProvider;

}
