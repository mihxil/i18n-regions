module org.meeuw.i18n.regions {
    exports org.meeuw.i18n.regions.bind.jaxb;
    exports org.meeuw.i18n.regions.persistence;
    exports org.meeuw.i18n.regions.validation;
    exports org.meeuw.i18n.regions.validation.impl;
    exports org.meeuw.i18n.regions.spi;
    exports org.meeuw.i18n.regions;


    requires static java.validation;
    requires static java.xml.bind;
    requires static org.checkerframework.checker.qual;
    requires static java.persistence;
    requires static java.annotation;
    requires static com.fasterxml.jackson.databind;

    requires transitive nv.i18n;
    requires java.logging;
    requires org.meeuw.i18n.languages;

    uses org.meeuw.i18n.regions.spi.RegionProvider;
    uses java.util.spi.LocaleNameProvider;
    uses java.util.spi.LocaleServiceProvider;

}
