package org.meeuw.i18n.subdivisions.validation;

import be.olsson.i18n.subdivision.CountryCodeSubdivision;

import java.util.Optional;
import java.util.stream.Stream;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.meeuw.i18n.RegionService;
import org.meeuw.i18n.subdivisions.CountrySubdivision;
import org.meeuw.i18n.subdivisions.CountrySubdivisionWithCode;
import org.meeuw.i18n.validation.impl.RegionConstraintValidator;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class CountrySubdivisionConstraintValidator implements ConstraintValidator<ValidCountrySubdivision, Object> {

    ValidationInfo validationInfo;

    @Override
    public void initialize(ValidCountrySubdivision constraintAnnotation) {
        this.validationInfo = ValidationInfo.from(constraintAnnotation);

    }

    @Override
    public boolean isValid(Object region, ConstraintValidatorContext constraintValidatorContext) {
        return isValid(region);
    }


    private boolean isValid(Object region) {
        if (region == null) {
            return true;
        }
        if (region instanceof Iterable) {
            Iterable <?> i = (Iterable) region;
            for (Object o : i) {
                if (! isValid(o)) {
                    return false;
                }
            }
            return true;
        } else {
            CountrySubdivision c = convert(region);
            return isValid(c, validationInfo);
        }
    }


    private boolean isValid(CountrySubdivision region, ValidationInfo validationInfo) {
        Optional<Boolean> aBoolean = RegionConstraintValidator.defaultIsValid(region, validationInfo);
        if (aBoolean.isPresent()) {
            return aBoolean.get();
        }
        if (Stream.of(validationInfo.excludeCountries).anyMatch(c ->  region.getCountryCode().equals(c))) {
            return false;
        }
        if (Stream.of(validationInfo.includeCountries).anyMatch(c ->  region.getCountryCode().equals(c))) {
            return true;
        }
        return false;
    }


    protected CountrySubdivision convert(Object o) {
        if (o instanceof CountrySubdivision) {
            return (CountrySubdivision) o;
        } else if (o instanceof CountryCodeSubdivision) {
            return new CountrySubdivisionWithCode((CountryCodeSubdivision) o);
        } else if (o instanceof CharSequence) {
            Optional<CountrySubdivision> byCode = RegionService.getInstance().getByCode(o .toString(), false, CountrySubdivision.class);
            return byCode.orElse(null);
        } else {
            return null;
        }
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
