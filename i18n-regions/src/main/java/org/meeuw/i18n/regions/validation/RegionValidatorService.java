package org.meeuw.i18n.regions.validation;

import java.util.Collections;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Logger;

import jakarta.validation.*;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.i18n.regions.RegionService;

/**
 * Makes validation settings (via annotation) available as a {@link Predicate}.
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class RegionValidatorService {
    private static final Logger logger = Logger.getLogger(RegionValidatorService.class.getName());



    private final static Validator VALIDATOR;

    static {
        Validator proposal;
        try {
            ValidatorFactory factory = Validation.byDefaultProvider()
                .configure()
                .buildValidatorFactory();

            proposal = factory.getValidator();
        } catch (Exception e) {
            logger.warning(e.getMessage());
            proposal = null;
        }
        VALIDATOR = proposal;
    }

    private static final RegionValidatorService INSTANCE = new RegionValidatorService();


    public static RegionValidatorService getInstance() {
        return INSTANCE;
    }

    private RegionValidatorService() {
    }

    /**
     * Returns the validation info for the given property of the name as a {@link Predicate}, which can e.g. be used to {@link java.util.stream.Stream#filter(Predicate)} the results of {@link RegionService#values()}
     * @param clazz
     * @param propertyName  The property which is annotation with jakarta.validation annotation's like {@link ValidRegion}.
     * @param groups
     * @return
     */
    public Predicate<Object> fromProperty(
        @NonNull Class<?> clazz,
        @NonNull String propertyName,
        @NonNull Class<?>... groups) {
        return (o) -> VALIDATOR.validateValue(clazz, propertyName, o, groups).isEmpty();
    }

      /**
     * Returns the validation info for the given property of the name as a {@link Predicate}, which can e.g. be used to {@link java.util.stream.Stream#filter(Predicate)} the results of {@link RegionService#values()}
     * @param clazz
     * @param propertyName  The property which is annotation with jakarta.validation annotation's like {@link ValidRegion}.
     * @param groups
     * @return
     */
    public Predicate<Object> fromListProperty(
        @NonNull Class<?> clazz,
        @NonNull String propertyName,
        @NonNull Class<?>... groups) {
        return fromWrappedProperty(clazz, propertyName, Collections::singletonList, groups);
    }

     public Predicate<Object> fromSetProperty(
        @NonNull Class<?> clazz,
        @NonNull String propertyName,
        @NonNull Class<?>... groups) {
        return fromWrappedProperty(clazz, propertyName, Collections::singleton, groups);
    }

    /**
     * Returns the validation info for the given property of the name as a {@link Predicate}, which can e.g. be used to {@link java.util.stream.Stream#filter(Predicate)} the results of {@link RegionService#values()}
     * @param clazz
     * @param propertyName  The property which is annotation with jakarta.validation annotation's like {@link ValidRegion}.
     * @param groups
     * @return
     */
    public @NonNull Predicate<Object> fromWrappedProperty(
        @NonNull Class<?> clazz,
        @NonNull String propertyName,
        @NonNull Function<Object, ?> wrapper,
        @NonNull Class<?>... groups
    ) {
        return o -> functionFromWrappedProperty(clazz, propertyName, wrapper, groups).apply(o).isEmpty();
    }

    public @NonNull Function<Object, Set<? extends ConstraintViolation<?>>> functionFromWrappedProperty(
        @NonNull Class<?> clazz,
        @NonNull String propertyName,
        @NonNull Function<Object, ?> wrapper,
        @NonNull Class<?>... groups
    ) {
        return o -> {
            Object apply = wrapper.apply(o);
            return VALIDATOR.validateValue(clazz, propertyName, apply, groups);
        };
    }

    public Validator getValidator() {
        return VALIDATOR;
    }

}
