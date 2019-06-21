package org.meeuw.i18n.continents;

import java.util.Locale;

import org.meeuw.i18n.Region;

/**
 * https://datahub.io/core/continent-codes

 * @author Michiel Meeuwissen
 * @since 0.1
 */
@SuppressWarnings("unused")
public class Continent implements Region {

    public static final String PREFIX = "CONTINENT-";

    private final Code code;


    public Continent(Code code) {
        this.code = code;
    }
    @Override
    public String toString() {
        return getCode();
    }


    @Override
    public String getCode() {
        return PREFIX + code.name();

    }

    @Override
    public Locale toLocale() {
        return null;

    }

    @Override
    public Type getType() {
        return Type.CONTINENT;

    }

    @Override
    public String getName() {
        return code.getName();

    }

    @Override
    public String getBundle() {
        return "Continents";

    }

    public enum Code {

        AF("Africa"),
        NA("North America"),
        OC("Oceania"),
        AN("Antarctica"),
        AS("Asia"),
        EU("Europe"),
        SA("South America");

        private final String name;

        Code(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
