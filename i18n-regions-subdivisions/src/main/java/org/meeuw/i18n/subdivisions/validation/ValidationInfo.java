package org.meeuw.i18n.subdivisions.validation;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
class ValidationInfo extends org.meeuw.i18n.validation.ValidationInfo {



    public ValidationInfo(String[] excludes, String[] includes, Class[] classes, int value) {
        super(excludes, includes, classes);
        }

    public static ValidationInfo from(ValidCountrySubdivision annotation) {
        return new ValidationInfo(annotation.excludes(), annotation.includes(), annotation.classes(), annotation.value());
    }
}
