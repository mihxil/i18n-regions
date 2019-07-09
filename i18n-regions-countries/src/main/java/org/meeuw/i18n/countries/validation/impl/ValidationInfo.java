package org.meeuw.i18n.countries.validation.impl;

import org.meeuw.i18n.Region;
import org.meeuw.i18n.countries.validation.ValidCountry;

/**
 * Contains the information of a {@link ValidCountry}. The advantage being that this class can be extended (an annotation cannot be)
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class ValidationInfo extends org.meeuw.i18n.validation.impl.ValidationInfo {

    final int value;

    protected ValidationInfo(String[] excludes, String[] includes, Class[] classes, Region.Type[] types, int value) {
        super(excludes, includes, classes, types);
        this.value = value;
    }

    public static ValidationInfo from(ValidCountry annotation) {
        return new ValidationInfo(annotation.excludes(), annotation.includes(), annotation.classes(), annotation.types(), annotation.value());
    }
}
