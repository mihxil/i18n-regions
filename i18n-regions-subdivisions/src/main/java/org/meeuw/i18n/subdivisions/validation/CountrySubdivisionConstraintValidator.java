package org.meeuw.i18n.subdivisions.validation;

import java.util.Optional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.meeuw.i18n.subdivisions.CountrySubdivision;
import org.meeuw.i18n.validation.RegionConstraintValidator;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class CountrySubdivisionConstraintValidator implements ConstraintValidator<ValidCountrySubdivision, Object> {



    protected boolean isValid(CountrySubdivision region, ValidationInfo validationInfo) {
        Optional<Boolean> aBoolean = RegionConstraintValidator.defaultIsValid(region, validationInfo);
        return aBoolean.orElse(true);
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        return false;

    }

    protected CountrySubdivision convert(Object o) {
        if (o instanceof CountrySubdivision) {
            return (CountrySubdivision) o;
        }
        return null;

    }
/*

    @Override
    public boolean isValid(Object region, ConstraintValidatorContext constraintValidatorContext) {
        return isValid(region, validationInfo);
    }

    protected boolean isValid(Object region) {
        if (region == null) {
            return true;
        }
        if (region instanceof Iterable) {
            Iterable <?> i = (Iterable) region;
            for (Object o : i) {
                if (! isValid(o, validationInfo)) {
                    return false;
                }
            }
            return true;
        } else if (region instanceof CountrySubdivision) {
            return isValid((CountrySubdivision) region, annotation).orElse(false);
        } else if (region instanceof CharSequence) {
            Optional<CountrySubdivision> byCode = RegionService.getInstance().getByCode(region.toString(), CountrySubdivision.class);
            return byCode.filter(value -> isValid(value, annotation).orElse(false)).isPresent();
        } else {
            return true;
        }
    }

    protected Optional<Boolean> isValid(CountrySubdivision region) {

        Optional<Boolean> superValid = RegionValidator.isValid(region);
        if (superValid.isPresent()) {
            return superValid;
        }

        return Optional.empty();
    }
*/



}
