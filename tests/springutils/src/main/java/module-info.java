module org.meeuw.springutils {
    requires transitive org.meeuw.i18n.regions;

    requires spring.web;

    requires static jakarta.servlet;

    requires org.apache.commons.lang3;
    requires spring.core;
    requires spring.context;
    exports org.meeuw.springutils;


}
