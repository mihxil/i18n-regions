package org.meeuw.i18n;

import com.neovisionaries.i18n.CountryCode;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public interface Country extends Region {


    @Override
    default Type getType() {
        return Type.COUNTRY;
    }

    String getAlpha2();
    String getAlpha3();

    int getNumeric();



    CountryCode.Assignment getAssignment();

}
