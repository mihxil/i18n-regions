package org.meeuw.i18n.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.meeuw.i18n.Country;
import org.meeuw.i18n.Region;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class RegionValidator implements ConstraintValidator<ValidRegion, Region> {

    ValidRegion annotation;
    @Override
    public void initialize(ValidRegion constraintAnnotation) {
        this.annotation = constraintAnnotation;

    }

    @Override
    public boolean isValid(Region region, ConstraintValidatorContext constraintValidatorContext) {

        if ((annotation.predicates() & ValidRegion.OFFICIAL) != 0) {
            if (Country.IS_OFFICIAL.test(region)) {
                return true;
            }
        }
        if ((annotation.predicates() & ValidRegion.FORMER) != 0) {
            if (Country.IS_FORMER.test(region)) {
                return true;
            }
        }
        if ((annotation.predicates() & ValidRegion.USER_ASSIGNED) != 0) {
            if (Country.IS_USER.test(region)) {
                return true;
            }
        }
        return false;

    }
}
