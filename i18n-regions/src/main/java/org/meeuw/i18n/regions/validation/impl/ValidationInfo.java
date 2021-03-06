package org.meeuw.i18n.regions.validation.impl;

import java.util.stream.Stream;

import org.meeuw.i18n.regions.Region;
import org.meeuw.i18n.regions.RegionService;
import org.meeuw.i18n.regions.validation.ValidRegion;

/**
 * Contains the information of a {@link ValidRegion}.
 * The advantage being that this class can be extended (an annotation cannot be)
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class ValidationInfo {

    final Region[] excludes;
    final Region[] includes;
    final Class[] classes;
    final Region.Type[] types;

    protected ValidationInfo(String[] excludes, String[] includes, Class[] classes, Region.Type[] types) {
        this.excludes = Stream.of(excludes)
            .map(r -> RegionService.getInstance().getByCode(r, false).orElseThrow(() -> new IllegalStateException("No such region " + r)))
            .toArray(Region[]::new);
        this.includes = Stream.of(includes)
            .map(r -> RegionService.getInstance().getByCode(r, false).orElseThrow(() -> new IllegalStateException("No such region " + r)))
            .toArray(Region[]::new);

        this.classes = classes;
        this.types = types;
    }

    public static ValidationInfo from(ValidRegion annotation) {
        return new ValidationInfo(annotation.excludes(), annotation.includes(), annotation.classes(), annotation.types());
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

    public Region.Type[] getTypes() {
        return types;
    }
}
