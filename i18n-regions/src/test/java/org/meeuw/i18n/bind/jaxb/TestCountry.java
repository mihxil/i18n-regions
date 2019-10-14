package org.meeuw.i18n.bind.jaxb;

import org.meeuw.i18n.Region;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
class TestCountry implements Region {
    private final String code;
    private final String name;

    TestCountry(String code, String name) {
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
    public String getBundle() {
        return "Country";

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestCountry country = (TestCountry) o;

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
