package org.meeuw.i18n.validation;

import java.util.stream.Stream;

import org.meeuw.i18n.Region;
import org.meeuw.i18n.RegionService;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class ValidationInfo {

    final Region[] excludes;
    final Region[] includes;
    final Class[] classes;

    protected ValidationInfo(String[] excludes, String[] includes, Class[] classes) {
        this.excludes = Stream.of(excludes)
            .map(r -> RegionService.getInstance().getByCode(r, false).orElseThrow(() -> new IllegalStateException("No such region " + r)))
            .toArray(Region[]::new);
        this.includes = Stream.of(includes)
            .map(r -> RegionService.getInstance().getByCode(r, false).orElseThrow(() -> new IllegalStateException("No such region " + r)))
            .toArray(Region[]::new);;
        this.classes = classes;
    }

    public static ValidationInfo from(ValidRegion annotation) {
        return new ValidationInfo(annotation.excludes(), annotation.includes(), annotation.classes());
    }

    public Region[] getExcludes() {
        return excludes;
    }

    public Region[] getIncludes() {
        return includes;
    }

    public Class[] getClasses() {
        return classes;
    }
}
