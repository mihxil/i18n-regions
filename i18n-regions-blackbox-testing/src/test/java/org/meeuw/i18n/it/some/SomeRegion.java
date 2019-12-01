package org.meeuw.i18n.it.some;

import org.meeuw.i18n.regions.Region;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
public class SomeRegion implements Region {

    private final String code;

    public SomeRegion(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;

    }

    @Override
    public Type getType() {
        return Type.SUBDIVISION;

    }

    @Override
    public String getName() {
        return getCode();

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SomeRegion that = (SomeRegion) o;

        return code.equals(that.code);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return getName();
    }
}
