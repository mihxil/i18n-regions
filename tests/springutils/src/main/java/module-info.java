module org.meeuw.springutils {
    requires transitive org.meeuw.i18n;

    requires spring.web;

    requires static javax.servlet.api;

    requires org.apache.commons.lang3;
    requires spring.core;
    requires spring.context;
    exports org.meeuw.springutils;


}
