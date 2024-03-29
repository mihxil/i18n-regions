package org.meeuw.i18n.regions;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * User assigned regions have a method {@link #getAssignedBy()}
 *
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public abstract class UserAssignedRegion implements Region {

    protected final String code;
    protected final String name;
    protected final String assignedBy;

    protected UserAssignedRegion(
        @NonNull String code,
        @NonNull String name, String assignedBy) {
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
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAssignedRegion that = (UserAssignedRegion) o;
        return code.equals(that.code);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }
}
