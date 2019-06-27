package org.meeuw.i18n.countries.validation;

import java.util.Optional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.meeuw.i18n.RegionService;
import org.meeuw.i18n.countries.Country;
import org.meeuw.i18n.formerlyassigned.FormerlyAssignedCountryCode;
import org.meeuw.i18n.validation.RegionConstraintValidator;

import com.neovisionaries.i18n.CountryCode;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class CountryConstraintValidator implements ConstraintValidator<ValidCountry, Object> {


    ValidationInfo validationInfo;

    @Override
    public void initialize(ValidCountry constraintAnnotation) {
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
            Country c = convert(region);
            return isValid(c, validationInfo);
        }
    }



    private boolean isValid(Country region, ValidationInfo validationInfo) {

        Optional<Boolean> aBoolean = RegionConstraintValidator.defaultIsValid(region, validationInfo);
        if (aBoolean.isPresent()) {
            return aBoolean.get();
        }

        if ((validationInfo.value & ValidCountry.OFFICIAL) != 0) {
            if (Country.IS_OFFICIAL.test(region)) {
                return true;
            }
        }
        if ((validationInfo.value & ValidCountry.FORMER) != 0) {
            if (Country.IS_FORMER.test(region)) {
                return true;
            }
        }
        if ((validationInfo.value & ValidCountry.USER_ASSIGNED) != 0) {
            if (Country.IS_USER_ASSIGNED.test(region)) {
                return true;
            }
        }



        return false;

    }


    private Country convert(Object o) {
        if (o instanceof Country) {
            return (Country) o;
        } else if (o instanceof CountryCode) {
            return Country.of((CountryCode) o);
        } else if (o instanceof FormerlyAssignedCountryCode) {
            return Country.of((FormerlyAssignedCountryCode) o);
        } else if (o instanceof CharSequence) {
            Optional<Country> byCode = RegionService.getInstance().getByCode(o .toString(), Country.class);
            return byCode.orElse(null);
        } else {
            return null;
        }
    }



}
