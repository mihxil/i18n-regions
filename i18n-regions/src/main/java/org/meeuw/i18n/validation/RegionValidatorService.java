package org.meeuw.i18n.validation;

import java.util.function.Predicate;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.i18n.RegionService;

/**

 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class RegionValidatorService {
    private static final ValidatorFactory FACTORY = Validation.buildDefaultValidatorFactory();
    private static final Validator VALIDATOR = FACTORY.getValidator();

    private static RegionValidatorService INSTANCE = new RegionValidatorService();

    public static RegionValidatorService getInstance() {
        return INSTANCE;
    }

    /**
     * Returns the validation info for the given property of the name as a {@link Predicate}, which can e.g. be used to {@link java.util.stream.Stream#filter(Predicate)} the results of {@link RegionService#values()}
     * @param clazz
     * @param propertyName  The property which is annotation with javax.validation annotation's like {@link ValidRegion}.
     * @param groups
     * @return
     */
    public Predicate<Object> fromProperty(
        @NonNull Class<?> clazz,
        @NonNull String propertyName,
        @NonNull Class<?>... groups) {
        return (o) -> VALIDATOR.validateValue(clazz, propertyName, o, groups).isEmpty();
    }

}
