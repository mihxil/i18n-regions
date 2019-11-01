import org.meeuw.i18n.regions.spi.*;
import org.meeuw.i18n.regions.testcountry.TestCountryProvider;

open module org.meeuw.i18n.regions {
    exports org.meeuw.i18n.regions.bind.jaxb;

    exports org.meeuw.i18n.regions.validation;
    exports org.meeuw.i18n.regions.validation.impl;
    exports org.meeuw.i18n.regions.spi;
    exports org.meeuw.i18n.regions;


    requires static java.validation;
    requires static java.xml.bind;
    requires static org.checkerframework.checker.qual;
    requires static java.persistence;
    requires static java.annotation;
    requires transitive nv.i18n;
    requires java.logging;

    requires static org.junit.jupiter.api;
    requires static org.junit.jupiter.params;
    requires static org.assertj.core;


    provides RegionProvider with

        TestCountryProvider;

}
