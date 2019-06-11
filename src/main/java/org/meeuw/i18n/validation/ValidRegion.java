package org.meeuw.i18n.validation;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public @interface ValidRegion {

    int OFFICIAL = 1 << 0;
    int FORMER = 1 << 1;
    int USER_ASSIGNED = 1 << 2;


    int predicates() default  OFFICIAL | FORMER | USER_ASSIGNED;






}
