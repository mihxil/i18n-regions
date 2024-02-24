module org.meeuw.i18n.regions {
    exports org.meeuw.i18n.regions.bind.jaxb;
    exports org.meeuw.i18n.regions.persistence;
    exports org.meeuw.i18n.regions.validation;
    exports org.meeuw.i18n.regions.spi;
    exports org.meeuw.i18n.regions;


    requires static jakarta.validation;
    requires static jakarta.xml.bind;
    requires static org.checkerframework.checker.qual;
    requires static jakarta.persistence;
    requires static jakarta.annotation;
    requires static com.fasterxml.jackson.databind;
    requires static org.meeuw.i18n.languages;
    
    requires transitive nv.i18n;
    requires java.logging;

    uses org.meeuw.i18n.regions.spi.RegionProvider;
    uses java.util.spi.LocaleNameProvider;
    uses java.util.spi.LocaleServiceProvider;
    
    // to validation implementations
    opens org.meeuw.i18n.regions.validation.impl;
    

    exports org.meeuw.i18n.regions.validation.impl to 
        org.meeuw.i18n.countries, // countries validation
        org.meeuw.i18n.subdivisions, // subdivisions validation
        org.meeuw.i18n.test.regions; // and testing
    
}
