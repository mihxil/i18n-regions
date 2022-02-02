package org.meeuw.i18n.subdivisions.validation.impl;

import org.meeuw.i18n.regions.Region;
import org.meeuw.i18n.subdivisions.validation.ValidCountrySubdivision;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
class ValidationInfo extends org.meeuw.i18n.regions.validation.impl.ValidationInfo {

    final org.meeuw.i18n.countries.validation.impl.ValidationInfo countryValidationInfo;

    protected ValidationInfo(
        String[] excludes,
        String[] includes,
        String[] excludeAssigners,
        String[] includeAssigners,
        Class<?>[] classes,
        Region.Type[] types,
        String[] codes,
        org.meeuw.i18n.countries.validation.impl.ValidationInfo countryValidationInfo
        ) {
        super(excludes, includes, excludeAssigners, includeAssigners, classes, types, codes);
        this.countryValidationInfo = countryValidationInfo;
    }

    static ValidationInfo from(ValidCountrySubdivision annotation) {
        return new ValidationInfo(
            annotation.excludes(), annotation.includes(),
            annotation.excludeAssigners(),
            annotation.includeAssigners(),
            annotation.classes(),
            annotation.types(),
            annotation.codes(),
            org.meeuw.i18n.countries.validation.impl.ValidationInfo.from(annotation.country())
        );
    }
}
