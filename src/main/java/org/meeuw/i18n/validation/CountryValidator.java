package org.meeuw.i18n.validation;

import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.meeuw.i18n.Country;
import org.meeuw.i18n.Region;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class CountryValidator implements ConstraintValidator<ValidCountry, Region> {

    ValidCountry annotation;
    @Override
    public void initialize(ValidCountry constraintAnnotation) {
        this.annotation = constraintAnnotation;

    }

    @Override
    public boolean isValid(Region region, ConstraintValidatorContext constraintValidatorContext) {
        return isValid(region, annotation);
    }


    public static boolean isValid(Region region, @Nonnull ValidCountry annotation) {
        if (region == null) {
            // use javax.validation.constraints.NotNull
            return true;
        }

        if (Stream.of(annotation.excludes()).anyMatch((r) -> region.getCode().equals(r))) {
            return false;
        }

        if ((annotation.value() & ValidCountry.OFFICIAL) != 0) {
            if (Country.IS_OFFICIAL.test(region)) {
                return true;
            }
        }
        if ((annotation.value() & ValidCountry.FORMER) != 0) {
            if (Country.IS_FORMER.test(region)) {
                return true;
            }
        }
        if ((annotation.value() & ValidCountry.USER_ASSIGNED) != 0) {
            if (Country.IS_USER_ASSIGNED.test(region)) {
                return true;
            }
        }
        if (Stream.of(annotation.includes()).anyMatch((r) -> region.getCode().equals(r))) {
            return true;
        }

        return false;
    }

    public static Predicate<Region> fromField(@Nonnull  Class<?> clazz, @Nonnull String field) throws NoSuchFieldException {
        ValidCountry[] annotationsByType = clazz.getDeclaredField(field).getAnnotationsByType(ValidCountry.class);
        if (annotationsByType.length == 0) {
            throw new IllegalArgumentException("No ValidCountry annotation on " + clazz.getSimpleName() + "#" + field);
        }
        Predicate<Region> predicate = r -> isValid(r, annotationsByType[0]);
        for (int i = 1; i < annotationsByType.length; i++) {
            final int index = i;
            predicate = predicate.and(r -> isValid(r, annotationsByType[index]));
        }
        return predicate;

    }
}
