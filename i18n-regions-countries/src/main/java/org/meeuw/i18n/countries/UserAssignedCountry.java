package org.meeuw.i18n.countries;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.i18n.regions.UserAssignedRegion;

import com.neovisionaries.i18n.CountryCode;

/**
 * Wraps all 'User defined' {@link CountryCode} (except {@link CountryCode#UNDEFINED} and assigns to each a {@link #getAssignedBy()}.
 *
 * Adds a few others, too.
 *
 * These are just the static constants in this class, which are automatically registered via {@link UserAssignedCountryProvider#register(UserAssignedCountry)}.
 *
 * http://en.wikipedia.org/wiki/ISO_3166-2
 * <a href="https://docs.google.com/spreadsheet/ccc?key=0Ajm-SImXl8LzdGVkU3ZJRV9JS3ZIVDZNNTd6eTVLSGc&amp;usp=sharing#gid=0">this doc</a>
 * @author Michiel Meeuwissen
 * @see UserAssignedCountryProvider
 * @since 0.1
 */
@SuppressWarnings("unused")
public class UserAssignedCountry extends UserAssignedRegion implements Country {

    public static final String ASSIGNER_UNICODE = "Unicode Common Locale Data Repository";
    public static final String ASSIGNER_EU     = "European Union";
    public static final String ASSIGNER_WIPO = "World Intellectual Property Organization";
    public static final String ASSIGNER_UN = "UN/LOCODE";


    public static final UserAssignedCountry XZ = new UserAssignedCountry("XZ", "International Waters",     ASSIGNER_UN);
    public static final UserAssignedCountry QO = new UserAssignedCountry("QO", "Outlying Oceania",         ASSIGNER_UNICODE);
    public static final UserAssignedCountry QU = new UserAssignedCountry("QU", "European Union", null, -1, ASSIGNER_UNICODE, CountryCode.EU);
    public static final UserAssignedCountry ZZ = new UserAssignedCountry("ZZ", "Unknown or Invalid Territory", ASSIGNER_UNICODE);
    public static final UserAssignedCountry XN = new UserAssignedCountry("XN", "Nordic Patent Institute",      ASSIGNER_WIPO);

    // There are the ones present in nv-18n
    public static final UserAssignedCountry XK = new UserAssignedCountry(CountryCode.XK,  ASSIGNER_EU);
    public static final UserAssignedCountry XI = new UserAssignedCountry(CountryCode.XI, ASSIGNER_EU);
    public static final UserAssignedCountry XU = new UserAssignedCountry(CountryCode.XU, ASSIGNER_EU);

    private final String alpha3;

    private final int number;

    private final CountryCode countryCode;

    protected UserAssignedCountry(
        @NonNull String code,
        @NonNull String name,
        String assignedBy) {
        this(code, name, null, -1, assignedBy, CountryCode.UNDEFINED);
    }

    protected UserAssignedCountry(
        @NonNull String code,
        @NonNull String name,
        String alpha3,
        int number,
        String assignedBy,
        CountryCode countryCode) {
        super(code, name, assignedBy);
        this.alpha3 = alpha3;
        this.number = number;
        this.countryCode = countryCode;
    }

    protected UserAssignedCountry(CountryCode code, String assignedBy) {
        this(code.getAlpha2(), code.getName(), code.getAlpha3(), code.getNumeric(), assignedBy, code);
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
    public CountryCode getCountryCode() {
        return countryCode;
    }

    @Override
    public String toString() {
        return getCode();
    }


}
