package org.meeuw.i18n.countries.validation;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class ValidationInfo extends org.meeuw.i18n.validation.ValidationInfo {

    final int value;

    protected ValidationInfo(String[] excludes, String[] includes, Class[] classes, int value) {
        super(excludes, includes, classes);
        this.value = value;
    }

    public static ValidationInfo from(ValidCountry annotation) {
        return new ValidationInfo(annotation.excludes(), annotation.includes(), annotation.classes(), annotation.value());
    }
}
