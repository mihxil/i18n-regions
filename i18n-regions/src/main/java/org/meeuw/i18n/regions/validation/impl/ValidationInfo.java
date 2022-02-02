package org.meeuw.i18n.regions.validation.impl;

import java.util.regex.Pattern;
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
    final Pattern[] excludeAssigners;
    final Pattern[] includeAssigners;

    final Class<?>[] classes;
    final Region.Type[] types;

    final String[] codes;


    protected ValidationInfo(
        String[] excludes,
        String[] includes,
        String[] excludeAssigners,
        String[] includeAssigners,
        Class<?>[] classes,
        Region.Type[] types,
        String[] codes) {
        this.excludes = Stream.of(excludes)
            .map(r -> RegionService.getInstance().getByCode(r, false).orElseThrow(() -> new IllegalStateException("No such region " + r)))
            .toArray(Region[]::new);
        this.includes = Stream.of(includes)
            .map(r -> RegionService.getInstance().getByCode(r, false).orElseThrow(() -> new IllegalStateException("No such region " + r)))
            .toArray(Region[]::new);
        this.excludeAssigners = Stream.of(excludeAssigners).map(Pattern::compile).toArray(Pattern[]::new);
        this.includeAssigners = Stream.of(includeAssigners).map(Pattern::compile).toArray(Pattern[]::new);
        this.classes = classes;
        this.types = types;
        this.codes = codes;
    }

    public static ValidationInfo from(ValidRegion annotation) {
        return new ValidationInfo(
            annotation.excludes(),
            annotation.includes(),
            annotation.excludeAssigners(),
            annotation.includeAssigners(),
            annotation.classes(),
            annotation.types(),
            annotation.codes()
        );
    }

    /**
     * @see ValidRegion#includes()
     */
    public Region[] getIncludes() {
        return includes;
    }

    /**
     * @see ValidRegion#excludes()
     */
    public Region[] getExcludes() {
        return excludes;
    }

    /**
     * @see ValidRegion#includeAssigners()
     */
    public Pattern[] getIncludeAssigners() {
        return includeAssigners;
    }

    /**
     * @see ValidRegion#excludeAssigners()
     */
    public Pattern[] getExcludeAssigners() {
        return excludeAssigners;
    }


    /**
     * @see ValidRegion#codes()
     */
    public String[] getCodes() {
        return codes;
    }

    /**
     * @see ValidRegion#classes()
     */
    public Class<?>[] getClasses() {
        return classes;
    }

    /**
     * @see ValidRegion#types()
     */
    public Region.Type[] getTypes() {
        return types;
    }
}
