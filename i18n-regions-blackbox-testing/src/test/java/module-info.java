import org.meeuw.i18n.it.some.SomeRegionProvider;
import org.meeuw.i18n.it.testcountry.TestCountryProvider;

open module org.meeuw.i18n.it {

    requires transitive org.meeuw.i18n.regions;

    requires static org.checkerframework.checker.qual;
    requires org.junit.jupiter.api;
    requires org.assertj.core;
    requires java.xml.bind;
    requires java.validation;
    requires org.meeuw.i18n.countries;
    requires org.meeuw.i18n.subdivisions;

    provides org.meeuw.i18n.regions.spi.RegionProvider with

        TestCountryProvider,
        SomeRegionProvider;

}
