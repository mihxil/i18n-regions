package org.meeuw.i18n.regions.validation.impl;

import java.util.*;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.validation.ConstraintValidatorContext;

import org.jspecify.annotations.Nullable;

/**
 * Resolves the default message templates of the constraints of this project.
 * <p>
 * The validation provider would normally resolve a template like {@code {org.meeuw.i18n.regions.validation.region.message}} itself, using
 * {@code ContributorValidationMessages} resource bundles. That does not work on the module path though: a validation provider running as a named
 * module (like hibernate-validator 9) cannot see resource bundles in other named modules. So a constraint validator resolves the template itself,
 * via a resource bundle in its own module, and gives the provider the resulting message template.
 * <p>
 * Expression language is normally not evaluated in templates created by constraint validators, so {@code ${validatedValue}} is replaced
 * here too (escaped, so it is not interpreted by the message interpolator).
 *
 * @author Michiel Meeuwissen
 * @since 3.3
 */
public class ValidationMessages {

    private static final Logger logger = Logger.getLogger(ValidationMessages.class.getName());

    static final String VALIDATED_VALUE = "${validatedValue}";

    private ValidationMessages() {
    }

    /**
     * If the default message template of the current constraint is a single key (like {@code {some.key}}) which is present in the resource bundle
     * returned by {@code bundle}, replaces the default constraint violation by one with the resolved message.
     *
     * @param value   The validated value
     * @param context The context of the {@link jakarta.validation.ConstraintValidator#isValid(Object, ConstraintValidatorContext)} call
     * @param bundle  Given a locale, returns the resource bundle. This should call {@link ResourceBundle#getBundle(String, Locale)} from the
     *                module containing the bundle, because that call is caller sensitive.
     */
    public static void resolveDefaultMessage(
        @Nullable Object value,
        @Nullable ConstraintValidatorContext context,
        Function<Locale, ResourceBundle> bundle) {
        if (context == null) {
            return;
        }
        String template = context.getDefaultConstraintMessageTemplate();
        if (template == null || template.length() < 3 || !template.startsWith("{") || !template.endsWith("}")) {
            return;
        }
        String key = template.substring(1, template.length() - 1);
        if (key.contains("{") || key.contains("}")) {
            return;
        }
        try {
            ResourceBundle resourceBundle = bundle.apply(Locale.getDefault());
            if (resourceBundle.containsKey(key)) {
                context.disableDefaultConstraintViolation();
                String message = resourceBundle.getString(key)
                    .replace(VALIDATED_VALUE, escape(String.valueOf(value)));
                context.buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation();
            }
        } catch (MissingResourceException mre) {
            logger.log(Level.FINE, mre.getMessage());
        }
    }

    /**
     * Escapes the characters that have a special meaning in message templates.
     */
    static String escape(String literal) {
        StringBuilder result = new StringBuilder(literal.length());
        for (char c : literal.toCharArray()) {
            if (c == '\\' || c == '{' || c == '}' || c == '$') {
                result.append('\\');
            }
            result.append(c);
        }
        return result.toString();
    }
}
