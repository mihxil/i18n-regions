package org.meeuw.i18n.countries.validation.impl;

import java.util.Locale;
import java.util.Optional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.meeuw.i18n.regions.RegionService;
import org.meeuw.i18n.countries.Country;
import org.meeuw.i18n.countries.validation.ValidCountry;
import org.meeuw.i18n.formerlyassigned.FormerlyAssignedCountryCode;
import org.meeuw.i18n.regions.validation.impl.RegionConstraintValidator;

import com.neovisionaries.i18n.CountryCode;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class CountryConstraintValidator implements ConstraintValidator<ValidCountry, Object> {


    private ValidationInfo validationInfo;

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
            Optional<Country> c = convert(region);
            if (c.isPresent()) {
                return isValid(c.get(), validationInfo);
            } else {
                // value is not a country, consider it valid, use @ValidRegion
                return true;
            }
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
            return Country.IS_USER_ASSIGNED.test(region);
        }

        return false;
    }


    private Optional<Country> convert(Object o) {
        if (o instanceof Country) {
            return Optional.of((Country) o);
        } else if (o instanceof CountryCode) {
            return Optional.of(Country.of((CountryCode) o));
        } else if (o instanceof FormerlyAssignedCountryCode) {
            return Optional.of(Country.of((FormerlyAssignedCountryCode) o));
        } else if (o instanceof CharSequence) {
            return RegionService.getInstance().getByCode(o.toString(), false, Country.class);
        } else if (o instanceof Locale) {
            String countryCode = ((Locale) o).getCountry();
            if (countryCode.isEmpty()) {
                return Optional.empty();
            } else {
                return RegionService.getInstance().getByCode(countryCode, false, Country.class);
            }
        } else {
            return Optional.empty();
        }
    }



}
