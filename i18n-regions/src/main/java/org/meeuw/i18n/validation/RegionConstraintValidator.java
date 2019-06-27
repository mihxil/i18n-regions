package org.meeuw.i18n.validation;

import java.util.Optional;
import java.util.stream.Stream;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.meeuw.i18n.Region;
import org.meeuw.i18n.RegionService;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class RegionConstraintValidator implements ConstraintValidator<ValidRegion, Object> {

    private ValidationInfo validationInfo;

    @Override
    public void initialize(ValidRegion constraintAnnotation) {
        this.validationInfo = ValidationInfo.from(constraintAnnotation);

    }

    @Override
    public boolean isValid(Object region, ConstraintValidatorContext constraintValidatorContext) {
        return isValidRegion(region);
    }

    protected boolean isValidRegion(Object region) {
        if (region == null) {
            return true;
        }
        if (region instanceof Iterable) {
            Iterable <?> i = (Iterable) region;
            for (Object o : i) {
                if (! isValidRegion(o)) {
                    return false;
                }
            }
            return true;
        } else {
            Region r = convert(region);
            return isValid(r, validationInfo);
        }
    }

    private boolean isValid(Region region, ValidationInfo validationInfo) {
        Optional<Boolean> aBoolean = defaultIsValid(region, validationInfo);
        return aBoolean.orElse(true);


    }

    private Region convert(Object o) {
        if(o instanceof Region) {
            return (Region) o;
        } else if (o instanceof CharSequence) {
            Optional<Region> byCode = RegionService.getInstance().getByCode(o.toString(), false);
            return byCode.orElseThrow(IllegalArgumentException::new);
        } else {
            throw new IllegalArgumentException("The object " + o + " cannot be converted to a region");
        }

    }



    public static Optional<Boolean> defaultIsValid(Region region, ValidationInfo validationInfo) {
        if (region == null) {
            return Optional.of(true);
        }
        if (Stream.of(validationInfo.getExcludes()).anyMatch(region::equals)) {
            return  Optional.of(false);
        }
        if (Stream.of(validationInfo.getIncludes()).anyMatch(region::equals)) {
            return Optional.of(true);
        }
        if (Stream.of(validationInfo.getClasses()).noneMatch((r) -> r.isInstance(region))) {
            return Optional.of(false);
        }
        return Optional.empty();
    }




}
