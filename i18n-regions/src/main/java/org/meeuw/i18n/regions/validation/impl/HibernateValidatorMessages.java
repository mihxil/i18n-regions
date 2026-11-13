package org.meeuw.i18n.regions.validation.impl;

import java.util.Formattable;

import jakarta.validation.ConstraintValidatorContext;

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.hibernate.validator.messageinterpolation.ExpressionLanguageFeatureLevel;

/**
 * Hibernate validator specific part of {@link ValidationMessages}. In a separate class, so that hibernate validator classes are only loaded if
 * hibernate validator is actually present.
 *
 * @author Michiel Meeuwissen
 * @since 3.4
 */
final class HibernateValidatorMessages {

    static final String VARIABLE = "org_meeuw_i18n_regions_message";

    /**
     * Use the template {@code ${formatter.format('%s', message)}}, so that the message is resolved by the message interpolator, in its own locale.
     * The {@code formatter} variable is defined by the Jakarta Validation specification.
     */
    static final String TEMPLATE = "${formatter.format('%s', " + VARIABLE + ")}";

    private HibernateValidatorMessages() {
    }

    static void defer(ConstraintValidatorContext context, Formattable message) {
        HibernateConstraintValidatorContext hibernateContext = context.unwrap(HibernateConstraintValidatorContext.class);
        hibernateContext.addExpressionVariable(VARIABLE, message);
        hibernateContext.buildConstraintViolationWithTemplate(TEMPLATE)
            .enableExpressionLanguage(ExpressionLanguageFeatureLevel.VARIABLES)
            .addConstraintViolation();
    }
}
