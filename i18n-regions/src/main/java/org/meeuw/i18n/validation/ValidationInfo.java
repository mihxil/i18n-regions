package org.meeuw.i18n.validation;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class ValidationInfo {

    final String[] excludes;
    final String[] includes;
    final Class[] classes;

    protected ValidationInfo(String[] excludes, String[] includes, Class[] classes) {
        this.excludes = excludes;
        this.includes = includes;
        this.classes = classes;
    }

    public static ValidationInfo from(ValidRegion annotation) {
        return new ValidationInfo(annotation.excludes(), annotation.includes(), annotation.classes());
    }

    public String[] getExcludes() {
        return excludes;
    }

    public String[] getIncludes() {
        return includes;
    }

    public Class[] getClasses() {
        return classes;
    }
}
