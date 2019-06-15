package org.meeuw.i18n;

import java.util.Locale;

/**
 *
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public abstract class UserAssigned implements Region {

    protected final String code;
    protected final String name;
    protected final String assignedBy;

    protected UserAssigned(String code, String name, String assignedBy) {
        this.code = code;
        this.name = name;
        this.assignedBy = assignedBy;
    }

    @Override
    public String getCode() {
        return code;

    }

    public String getAssignedBy() {
        return assignedBy;
    }

    @Override
    public Locale toLocale() {
        return null;
    }

    @Override
    public String getName() {
        return name;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAssigned that = (UserAssigned) o;
        return code.equals(that.code);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }
}
