module org.meeuw.springutils {
    requires transitive org.meeuw.i18n.regions;

    requires spring.web;

    requires static jakarta.servlet;

    requires spring.core;
    requires spring.context;
    requires org.apache.commons.lang3;
    requires org.meeuw.functional;
    exports org.meeuw.springutils;


}
