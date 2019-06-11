package org.meeuw.i18n.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Target({FIELD, METHOD, TYPE_PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = RegionValidator.class)
@Documented
public @interface ValidRegion {

    int OFFICIAL = 1 << 0;
    int FORMER = 1 << 1;
    int USER_ASSIGNED = 1 << 2;

    String message() default "{org.meeuw.i18n.constraints.region}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


    int predicates() default  OFFICIAL | FORMER | USER_ASSIGNED;






}
