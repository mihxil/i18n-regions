package org.meeuw.i18n.regions.validation.impl;

import java.util.*;
import java.util.stream.Stream;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.checker.optional.qual.MaybePresent;
import org.meeuw.i18n.regions.Region;
import org.meeuw.i18n.regions.RegionService;
import org.meeuw.i18n.regions.validation.ValidRegion;

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
    public boolean isValid(@Nullable Object region, ConstraintValidatorContext constraintValidatorContext) {
        return isValidRegion(region);
    }

    protected boolean isValidRegion(@Nullable Object region) {
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
            ConvertResult r = convert(region);
            if (r.shouldValidate) {
                if (! r.isPresent()) {
                    // not convertible to Region, consider this value invalid
                    return false;
                } else {
                    return isValid(r.region.get(), validationInfo);
                }
            } else {
                return true;
            }
        }
    }

    private boolean isValid(Region region, ValidationInfo validationInfo) {
        Optional<Boolean> aBoolean = defaultIsValid(region, validationInfo);
        return aBoolean.orElse(true);


    }

    private ConvertResult convert(Object o) {
        if(o instanceof Region) {
            return ConvertResult.of((Region) o);
        } else if (o instanceof CharSequence) {
            return ConvertResult.of(RegionService.getInstance().getByCode(o.toString(), false));
        } else if (o instanceof Locale){

            String c  = ((Locale) o).getCountry();
            if (c.isEmpty()) {
                return ConvertResult.NOT_APPLICABLE;
            } else {
                Optional<Region> byCountry = RegionService.getInstance().getByCode(((Locale) o).getCountry(), false);
                return ConvertResult.of(byCountry);
            }
        } else {
            return ConvertResult.INVALID;
        }
    }


    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private static class ConvertResult {
        final Optional<Region> region;
        final boolean shouldValidate;

        public ConvertResult(@NonNull Optional<Region> region, boolean shouldValidate) {
            this.region = region;
            this.shouldValidate = shouldValidate;
        }
        public boolean isPresent() {
            return region.isPresent();
        }

        public static ConvertResult of(@NonNull Region region) {
            return of(Optional.of(region));
        }
        public static ConvertResult of(@NonNull Optional<Region> region) {
            return new ConvertResult(region, true);
        }
        public static final ConvertResult NOT_APPLICABLE = new ConvertResult(Optional.empty(), false);

        public static final ConvertResult INVALID  = new ConvertResult(Optional.empty(), true);

    }


    public static @MaybePresent Optional<Boolean> defaultIsValid(@Nullable Region region, ValidationInfo validationInfo) {
        if (region == null) {
            return Optional.of(true);
        }
        if (Arrays.asList(validationInfo.getExcludes()).contains(region)) {
            return  Optional.of(false);
        }
        if (Arrays.asList(validationInfo.getIncludes()).contains(region)) {
            return Optional.of(true);
        }
        if (Stream.of(validationInfo.getClasses()).noneMatch((r) -> r.isInstance(region))) {
            return Optional.of(false);
        }

        if (validationInfo.getTypes().length > 0) {
            if (Stream.of(validationInfo.getTypes()).noneMatch((t) -> region.getType() == t)) {
                return Optional.of(false);
            }
        }
        return Optional.empty();
    }




}
