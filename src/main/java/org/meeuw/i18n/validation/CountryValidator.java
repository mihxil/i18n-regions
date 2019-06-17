package org.meeuw.i18n.validation;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.meeuw.i18n.Country;
import org.meeuw.i18n.FormerlyAssignedCountryCode;
import org.meeuw.i18n.Region;
import org.meeuw.i18n.Regions;
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
        if (region == null) {
            return true;
        }
        if (region instanceof Iterable) {
            Iterable <?> i = (Iterable) region;
            for (Object o : i) {
                if (! isValid(o, constraintValidatorContext)) {
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
            Optional<Region> byCode = Regions.getByCode(region.toString());
            return byCode.filter(value -> isValid(value, annotation)).isPresent();
        } else {
            throw new IllegalArgumentException("The object " + region.getClass().getSimpleName() + ":" + region + " cannot be converted to a region");
        }
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

    public static Predicate<Region> fromField(@Nonnull  Class<?> clazz, @Nonnull String field) {
        try {
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
        } catch (NoSuchFieldException nsfe){
            throw new RuntimeException(nsfe);
        }

    }
}
