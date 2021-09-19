package org.meeuw.i18n.countries.validation.impl;

import org.meeuw.i18n.regions.Region;
import org.meeuw.i18n.countries.validation.ValidCountry;

/**
 * Contains the information of a {@link ValidCountry}. The advantage being that this class can be extended (an annotation cannot be)
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class ValidationInfo extends org.meeuw.i18n.regions.validation.impl.ValidationInfo {

    final int value;

    protected ValidationInfo(String[] excludes, String[] includes,
                             String[] excludeAssigners,
                             String[] includeAssigners,
                             Class<?>[] classes, Region.Type[] types, int value) {
        super(excludes, includes, excludeAssigners, includeAssigners, classes, types);
        this.value = value;
    }

    public static ValidationInfo from(ValidCountry annotation) {
        return new ValidationInfo(
            annotation.excludes(),
            annotation.includes(),
            annotation.excludeAssigners(),
            annotation.includeAssigners(),
            annotation.classes(), annotation.types(), annotation.value());
    }
}
