package org.meeuw.i18n.validation;

import java.util.function.Predicate;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.checkerframework.checker.nullness.qual.NonNull;

/**

 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class RegionValidatorService {
    private static final ValidatorFactory FACTORY = Validation.buildDefaultValidatorFactory();
    private static final Validator VALIDATOR = FACTORY.getValidator();


    public static Predicate<Object> fromProperty(
        @NonNull Class<?> clazz,
        @NonNull String propertyName,
        @NonNull Class<?>... groups) {
        return (o) -> VALIDATOR.validateValue(clazz, propertyName, o, groups).isEmpty();
    }

}
