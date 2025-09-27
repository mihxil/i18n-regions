package org.meeuw.i18n.subdivisions.validation.impl;

import java.util.Optional;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.meeuw.i18n.countries.Country;
import org.meeuw.i18n.countries.validation.impl.CountryConstraintValidator;
import org.meeuw.i18n.regions.RegionService;
import org.meeuw.i18n.regions.validation.impl.RegionConstraintValidator;
import org.meeuw.i18n.subdivision.CountrySubdivision;
import org.meeuw.i18n.subdivisions.CountrySubdivisionProvider;
import org.meeuw.i18n.subdivisions.validation.ValidCountrySubdivision;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class CountrySubdivisionConstraintValidator implements ConstraintValidator<ValidCountrySubdivision, Object> {

    private ValidationInfo validationInfo;

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
            try {
                Optional<CountrySubdivision> c = convert(region);
                if (!c.isPresent() && region instanceof CharSequence) {
                    // not found, but perhaps the _country_ was still valid
                    Optional<CountrySubdivisionProvider.CountryAndSubdivisionCode> of = CountrySubdivisionProvider.CountryAndSubdivisionCode.of((String) region);
                    if (of.isPresent() && of.get().getCountry() != null) {

                        Optional<Boolean> countryValidation = isValid(of.get().getCountry(), validationInfo);
                        if (countryValidation.isPresent() && !countryValidation.get()) {
                            return false;
                        }
                    }
                }
                // Value could not be converted to a country subdivision, consider it valid.
                // Use @RegionValidator to constrain that.
                return c
                    .map(countrySubdivision -> isValid(countrySubdivision, validationInfo))
                    .orElse(true);
            } catch (IllegalArgumentException iae) {
                // The specified _country_ does not exist
                return false;
            }

        }
    }


    private boolean isValid(CountrySubdivision region, ValidationInfo validationInfo) {
        Optional<Boolean> aBoolean = RegionConstraintValidator.defaultIsValid(region, validationInfo);
        if (aBoolean.isPresent()) {
            return aBoolean.get();
        }
        Optional<Boolean> countryValid = isValid(region.getCountry(), validationInfo);
        return countryValid.orElse(false);
    }

    private Optional<Boolean> isValid(final Country country, ValidationInfo validationInfo) {
        if (validationInfo.countryValidationInfo != null) {
            return Optional.of(CountryConstraintValidator.isValid(country, validationInfo.countryValidationInfo));
        }
        return Optional.empty();
    }


    protected Optional<CountrySubdivision> convert(Object o) {
        if (o instanceof CountrySubdivision) {
            return Optional.of((CountrySubdivision) o);
        } else if (o instanceof CountrySubdivision) {
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
