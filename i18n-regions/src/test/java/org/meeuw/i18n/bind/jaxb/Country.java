package org.meeuw.i18n.bind.jaxb;

import org.meeuw.i18n.Region;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
class Country implements Region {
    private final String code;
    private final String name;

    Country(String code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public Type getType() {
        return Type.COUNTRY;

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Country country = (Country) o;

        return code.equals(country.code);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return "A:" + code;
    }
}
