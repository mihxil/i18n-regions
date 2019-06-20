package org.meeuw.i18n.countries.validation;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.i18n.Region;
import org.meeuw.i18n.RegionService;
import org.meeuw.i18n.countries.Country;
import org.meeuw.i18n.formerlyassigned.FormerlyAssignedCountryCode;

import com.neovisionaries.i18n.CountryCode;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class CountryValidator implements ConstraintValidator<ValidCountry, Object> {

    ValidCountry annotation;
    @Override
    public void initialize(ValidCountry constraintAnnotation) {
        this.annotation = constraintAnnotation;

    }

    @Override
    public boolean isValid(Object region, ConstraintValidatorContext constraintValidatorContext) {
        return isValid(region, annotation);
    }

    public static boolean isValid(Object region, ValidCountry annotation) {
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
        } else if (region instanceof CountryCode) {
            return isValid(Country.of((CountryCode) region), annotation);
        } else if (region instanceof FormerlyAssignedCountryCode) {
            return isValid(Country.of((FormerlyAssignedCountryCode) region), annotation);
        } else if (region instanceof CharSequence) {
            Optional<Region> byCode = RegionService.getInstance().getByCode(region.toString());
            return byCode.filter(value -> isValid(value, annotation)).isPresent();
        } else {
            throw new IllegalArgumentException("The object " + region.getClass().getSimpleName() + ":" + region + " cannot be converted to a region");
        }
    }



    public static boolean isValid(Region region, @NonNull ValidCountry annotation) {
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

    public static Predicate<Object> fromField(@NonNull Class<?> clazz, @NonNull String field) {
        try {
            ValidCountry[] annotationsByType = clazz.getDeclaredField(field).getAnnotationsByType(ValidCountry.class);
            if (annotationsByType.length == 0) {
                throw new IllegalArgumentException("No ValidCountry annotation on " + clazz.getSimpleName() + "#" + field);
            }
            return fromAnnotations(annotationsByType);
        } catch (NoSuchFieldException nsfe){
            throw new RuntimeException(nsfe);
        }

    }

    public static Predicate<Object> fromMethod(@NonNull Method method) {
        ValidCountry[] annotationsByType = method.getAnnotationsByType(ValidCountry.class);
        if (annotationsByType.length == 0) {
            throw new IllegalArgumentException("No ValidCountry annotation on " + method);
        }
        return fromAnnotations(annotationsByType);
    }

    public static Predicate<Object> fromAnnotations(ValidCountry[] annotations) {
        Predicate<Object> predicate = r -> isValid(r, annotations[0]);
        for (int i = 1; i < annotations.length; i++) {
            final int index = i;
            predicate = predicate.and(r -> isValid(r, annotations[index]));
        }
        return predicate;

    }
}
