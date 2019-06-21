package org.meeuw.i18n.validation;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
public class ValidationInfo {

    final String[] excludes;
    final String[] includes;
    final Class[] classes;

    public ValidationInfo(String[] excludes, String[] includes, Class[] classes) {
        this.excludes = excludes;
        this.includes = includes;
        this.classes = classes;
    }

    public static ValidationInfo from(ValidRegion annotation) {
        return new ValidationInfo(annotation.excludes(), annotation.includes(), annotation.classes());
    }
}
