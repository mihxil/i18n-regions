package org.meeuw.i18n.subdivisions.validation.impl;

import org.meeuw.i18n.subdivisions.validation.ValidCountrySubdivision;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
class ValidationInfo extends org.meeuw.i18n.validation.impl.ValidationInfo {

    final String[] includeCountries;

    final String[] excludeCountries;

    protected ValidationInfo(String[] excludes, String[] includes, Class[] classes, String[] includeCountries, String[] excludeCountries) {
        super(excludes, includes, classes);
        this.includeCountries = includeCountries;
        this.excludeCountries = excludeCountries;
    }

    static ValidationInfo from(ValidCountrySubdivision annotation) {
        return new ValidationInfo(annotation.excludes(), annotation.includes(), annotation.classes(), annotation.includeCountries(), annotation.excludeCountries());
    }
}
