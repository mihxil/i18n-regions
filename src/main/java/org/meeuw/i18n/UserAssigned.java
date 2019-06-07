package org.meeuw.i18n;

import java.util.Locale;

import com.neovisionaries.i18n.CountryCode;

/**
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
    public String getISOCode() {
        return code;

    }

    public String getAssignedBy() {
        return assignedBy;
    }

    @Override
    public Locale toLocale() {
        return null;

    }


    public CountryCode.Assignment getAssignment() {
        return CountryCode.Assignment.USER_ASSIGNED;

    }

    @Override
    public String getName() {
        return name;

    }
}
