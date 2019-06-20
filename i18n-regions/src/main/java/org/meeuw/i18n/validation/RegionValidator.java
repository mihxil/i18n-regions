package org.meeuw.i18n.validation;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.i18n.Region;
import org.meeuw.i18n.RegionService;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class RegionValidator implements ConstraintValidator<ValidRegion, Object> {

    ValidRegion annotation;
    @Override
    public void initialize(ValidRegion constraintAnnotation) {
        this.annotation = constraintAnnotation;

    }

    @Override
    public boolean isValid(Object region, ConstraintValidatorContext constraintValidatorContext) {
        return isValid(region, annotation);
    }

    public static boolean isValid(Object region, ValidRegion annotation) {
        if (region == null) {
            return true;
        }
        if (region instanceof Iterable) {
            Iterable <?> i = (Iterable) region;
            for (Object o : i) {
                if (! isValid(o, annotation)) {
                    return false;
                }
            }
            return true;
        } else if (region instanceof Region) {
            return isValid((Region) region, annotation);
        } else if (region instanceof CharSequence) {
            Optional<Region> byCode = RegionService.getInstance().getByCode(region.toString());
            return byCode.filter(value -> isValid(value, annotation)).isPresent();
        } else {
            throw new IllegalArgumentException("The object " + region.getClass().getSimpleName() + ":" + region + " cannot be converted to a region");
        }
    }



    public static boolean isValid(Region region, @NonNull ValidRegion annotation) {
        if (region == null) {
            // use javax.validation.constraints.NotNull
            return true;
        }

        if (Stream.of(annotation.excludes()).anyMatch((r) -> region.getCode().equals(r))) {
            return false;
        }

        if (Stream.of(annotation.includes()).anyMatch((r) -> region.getCode().equals(r))) {
            return true;
        }

        return false;
    }

    public static Predicate<Object> fromField(@NonNull Class<?> clazz, @NonNull String field) {
        try {
            ValidRegion[] annotationsByType = clazz.getDeclaredField(field).getAnnotationsByType(ValidRegion.class);
            if (annotationsByType.length == 0) {
                throw new IllegalArgumentException("No ValidCountry annotation on " + clazz.getSimpleName() + "#" + field);
            }
            return fromAnnotations(annotationsByType);
        } catch (NoSuchFieldException nsfe){
            throw new RuntimeException(nsfe);
        }

    }

    public static Predicate<Object> fromMethod(@NonNull Method method) {
        ValidRegion[] annotationsByType = method.getAnnotationsByType(ValidRegion.class);
        if (annotationsByType.length == 0) {
            throw new IllegalArgumentException("No ValidCountry annotation on " + method);
        }
        return fromAnnotations(annotationsByType);
    }

    public static Predicate<Object> fromAnnotations(ValidRegion[] annotations) {
        Predicate<Object> predicate = r -> isValid(r, annotations[0]);
        for (int i = 1; i < annotations.length; i++) {
            final int index = i;
            predicate = predicate.and(r -> isValid(r, annotations[index]));
        }
        return predicate;

    }
}
