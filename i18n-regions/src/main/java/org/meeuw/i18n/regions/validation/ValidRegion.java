package org.meeuw.i18n.regions.validation;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import org.meeuw.i18n.regions.Region;
import org.meeuw.i18n.regions.validation.impl.RegionConstraintValidator;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * A {@code jakarta.validation} annotation that can be used to restrict the values of a {@link Region} values.
 * <p>
 * It recognized several {@link Class types} of objects.
 * If an object is a {@link Region}, or convertible to a {@code Region}, then it will be valid if the {@link Region#getType() type}
 * and {@link Class classes} are as specified (by {@link #types()} and {@link #classes()}.
 * <p>
 * Locales are only validated if the 'country' part is filled, and then only are valid if the country is filled and recognized by the RegionsService and its type is COUNTRY (or unspecified).
 *
 *
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE, TYPE_PARAMETER })
@Retention(RUNTIME)
@Constraint(validatedBy = RegionConstraintValidator.class)
@Documented
public @interface ValidRegion {

    String message() default "{org.meeuw.i18n.regions.validation.region.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * A list of codes to exclude. They are invalid regardless of {@link #classes()}
     *
     * So this provides a list of codes to consider invalid, regardless of other criteria.
     */
    String[] excludes() default {};

    /**
     * A list of assigners to excludes (only relevant if the type is {@link org.meeuw.i18n.regions.UserAssignedRegion}
     * @since 1.4
     */
    String[] excludeAssigners() default {};

    /**
     * A list of assigners to include (only relevant if the type is {@link org.meeuw.i18n.regions.UserAssignedRegion}
     * @since 1.4
     */
    String[] includeAssigners() default {};

    /**
     * A list of codes to include. They are valid (unless they are also in {@link #excludes()}, regardless of {@link #classes()}
     *
     * So this provides a list of <em>extra</em> codes to consider valid
     */
    String[] includes() default {};

     /**
      * A list of types. The default empty array will allow for any type, or at least leave that unspecified. When validating {@link java.util.Locale} it may default to {@link Region.Type#COUNTRY}.
     */
    Region.Type[] types() default {};

    /**
     * A list of classes (extensions of {@link Region}) that are to be considered valid.
     * Defaults to {@link Region Region.class} (i.e. no regions are invalid because of their {@link Object#getClass() class})
     */
    Class<? extends Region>[] classes() default {Region.class};


    /**
     * This defines the original list before any of the other criteria are applied. Unless it is empty (the default), which means <em>all</em> regions.
     */
    String[] codes() default {};

}
