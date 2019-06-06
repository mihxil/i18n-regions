package org.meeuw.i18n;

import com.neovisionaries.i18n.CountryCode;

/**
 * @author Michiel Meeuwissen
 * @since 3.8
 */
public class Utils {

    public static CountryCode getByCode(String s) {
        CountryCode code = CountryCode.getByCode(s);
        if (code == null) {
            return VehicleRegistrationCode.valueOf(s).getCode();
        } else {
            return code;
        }

    }
}
