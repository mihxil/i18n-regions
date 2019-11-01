package org.meeuw.i18n.countries;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.i18n.regions.UserAssignedRegion;

/**
 * http://en.wikipedia.org/wiki/ISO_3166-2
 * https://docs.google.com/spreadsheet/ccc?key=0Ajm-SImXl8LzdGVkU3ZJRV9JS3ZIVDZNNTd6eTVLSGc&usp=sharing#gid=0
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@SuppressWarnings("unused")
public class UserAssignedCountry extends UserAssignedRegion implements Country {

    public static UserAssignedCountry XZ =
        new UserAssignedCountry("XZ", "International Waters", null, -1, "UN/LOCODE");
    public static UserAssignedCountry QO =
        new UserAssignedCountry("QO", "Outlying Oceania", null, -1, "By  Unicode Common Locale Data Repository");

    public static UserAssignedCountry QU =
        new UserAssignedCountry("QU", "European Union", null, -1, "Unicode Common Locale Data Repository");
    public static UserAssignedCountry ZZ =
        new UserAssignedCountry("ZZ", "Unknown or Invalid Territory", null, -1, "Unicode Common Locale Data Repository");
    public static UserAssignedCountry XK = new UserAssignedCountry("XK", "Kosovo", "XKX", -1, "European Union");
    public static UserAssignedCountry XN = new UserAssignedCountry("XN", "Nordic Patent Institute", null, -1, "World Intellectual Property Organization");



    private final String alpha3;

    private final int number;

    public UserAssignedCountry(
        @NonNull String code,
        @NonNull String name, String alpha3, int number, String assignedBy) {
        super(code, name, assignedBy);
        this.alpha3 = alpha3;
        this.number = number;
    }

    public String getAlpha2() {
        return code;
    }

    public String getAlpha3() {
        return alpha3;

    }

    @Override
    public int getNumeric() {
        return number;

    }

    @Override
    public String getCountryCode() {
        return code;

    }

    @Override
    public String toString() {
        return getCode();
    }


}
