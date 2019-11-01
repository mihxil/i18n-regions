package org.meeuw.i18n.subdivisions.validation.impl;

import be.olsson.i18n.subdivision.CountryCodeSubdivision;

import java.util.Optional;
import java.util.stream.Stream;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.meeuw.i18n.regions.RegionService;
import org.meeuw.i18n.subdivisions.CountrySubdivision;
import org.meeuw.i18n.subdivisions.CountrySubdivisionWithCode;
import org.meeuw.i18n.subdivisions.validation.ValidCountrySubdivision;
import org.meeuw.i18n.regions.validation.impl.RegionConstraintValidator;

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
            Optional<CountrySubdivision> c = convert(region);
            if (c.isPresent()) {
                return isValid(c.get(), validationInfo);
            } else {
                // Value could not be converted to a country subdivision, consider it valid.
                // Use @RegionValidator to constrain that.
                return true;
            }
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


    protected Optional<CountrySubdivision> convert(Object o) {
        if (o instanceof CountrySubdivision) {
            return Optional.of((CountrySubdivision) o);
        } else if (o instanceof CountryCodeSubdivision) {
            return Optional.of(new CountrySubdivisionWithCode((CountryCodeSubdivision) o));
        } else if (o instanceof CharSequence) {
            return RegionService.getInstance().getByCode(o .toString(), false, CountrySubdivision.class);
        } else {
            return Optional.empty();
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
