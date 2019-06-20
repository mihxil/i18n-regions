/**
 * @author Michiel Meeuwissen
 * @since ...
 */
open module org.meeuw.springboot.jigsaw {

    requires org.meeuw.springutils;
    requires spring.boot.autoconfigure;
    requires spring.boot;

    uses org.meeuw.springutils.LocaleConverter;
}
