import org.meeuw.i18n.regions.spi.*;
import org.meeuw.i18n.regions.testcountry.TestCountryProvider;

open module org.meeuw.i18n.regions {
    exports org.meeuw.i18n.regions.bind.jaxb;

    exports org.meeuw.i18n.regions.validation;
    exports org.meeuw.i18n.regions.validation.impl;
    exports org.meeuw.i18n.regions.spi;
    exports org.meeuw.i18n.regions;


    requires static java.validation;
    requires static org.checkerframework.checker.qual;
    requires static java.persistence;
    requires static java.annotation;
    requires transitive nv.i18n;
    requires java.logging;

    requires org.junit.jupiter.api;
    requires org.junit.jupiter.params;
    requires org.assertj.core;
    requires java.xml.bind;



    provides RegionProvider with

        TestCountryProvider;

}
