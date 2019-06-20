module org.meeuw.i18n {
    exports org.meeuw.i18n;
    exports org.meeuw.i18n.bind.jaxb;
    exports org.meeuw.i18n.persistence;
    exports org.meeuw.i18n.validation;
    exports org.meeuw.i18n.spi;


    requires static java.validation;
    requires static java.xml.bind;
    requires static org.checkerframework.checker.qual;
    requires static java.persistence;
    requires java.annotation;
    requires transitive nv.i18n;

    uses org.meeuw.i18n.spi.RegionProvider;




}
