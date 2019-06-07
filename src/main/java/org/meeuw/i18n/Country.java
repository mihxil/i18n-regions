package org.meeuw.i18n;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public interface Country extends Region {


    @Override
    default Type getType() {
        return Type.COUNTRY;
    }

    String getISO3166_3_Code();

}
