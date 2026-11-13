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
 * via a resource bundle in its own module.
 * <p>
 * The message must be in the locale of the message interpolator, which is not necessarily {@link Locale#getDefault()} (e.g. spring interpolates in the
 * locale of the current request). A constraint validator cannot know that locale, so when possible, the actual resolving is deferred to the
 * message interpolator: with hibernate validator (and expression language available) the message becomes an expression variable, which is
 * {@link Formattable formatted} by the interpolator's {@code formatter}, which knows the locale. Otherwise, the message is resolved in
 * {@link Locale#getDefault()}.
 * <p>
 * The 'user' bundle {@code ValidationMessages} (in the root of the class path or of a module) is consulted first, so applications can
 * still override the messages, or add other languages, like they are used to with Jakarta Validation. This is done per key.
 *
 * @author Michiel Meeuwissen
 * @since 3.3
 */
public class ValidationMessages {

    private static final Logger logger = Logger.getLogger(ValidationMessages.class.getName());

    static final String VALIDATED_VALUE = "${validatedValue}";

    private static final boolean DEFER_TO_INTERPOLATOR = isPresent("org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext") && isPresent("jakarta.el.ExpressionFactory");

    private ValidationMessages() {
    }

    /**
     * If the default message template of the current constraint is a single key (like {@code {some.key}}) which is present in the resource bundle
     * returned by {@code bundle}, replaces the default constraint violation by one with the resolved message, in the locale of the message interpolator if possible.
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
        final LocalizedMessage message = new LocalizedMessage(key, value, bundle);
        final Optional<String> inDefaultLocale = message.template(Locale.getDefault());
        if (inDefaultLocale.isEmpty()) {
            // not one of our keys, leave it to the validation provider
            return;
        }
        context.disableDefaultConstraintViolation();
        if (DEFER_TO_INTERPOLATOR && deferToInterpolator(context, message)) {
            return;
        }
        context.buildConstraintViolationWithTemplate(inDefaultLocale.get().replace(VALIDATED_VALUE, escape(String.valueOf(value))))
            .addConstraintViolation();
    }

    private static boolean deferToInterpolator(ConstraintValidatorContext context, Formattable message) {
        try {
            HibernateValidatorMessages.defer(context, message);
            return true;
        } catch (RuntimeException | LinkageError e) {
            logger.log(Level.FINE, () -> "Could not defer message to interpolator: " + e.getClass().getName() + " " + e.getMessage());
            return false;
        }
    }

    private static boolean isPresent(String className) {
        try {
            Class.forName(className, false, ValidationMessages.class.getClassLoader());
            return true;
        } catch (ClassNotFoundException | LinkageError e) {
            return false;
        }
    }

    /**
     * A message which is only resolved when it is formatted, in the locale of the {@link Formatter}.
     */
    record LocalizedMessage(String key, @Nullable Object value, Function<Locale, ResourceBundle> bundle) implements Formattable {

        Optional<String> template(Locale locale) {
            return lookup(userBundle(locale), key)
                .or(() -> lookup(bundle(locale), key));
        }

        String message(@Nullable Locale locale) {
            return template(locale == null ? Locale.getDefault() : locale)
                .orElse("{" + key + "}")
                .replace(VALIDATED_VALUE, String.valueOf(value));
        }

        private @Nullable ResourceBundle bundle(Locale locale) {
            try {
                return withoutDefaultLocaleFallback(bundle.apply(locale), locale, () -> bundle.apply(Locale.ROOT));
            } catch (MissingResourceException mre) {
                logger.log(Level.FINER, mre.getMessage());
                return null;
            }
        }

        @Override
        public void formatTo(Formatter formatter, int flags, int width, int precision) {
            formatter.format("%s", message(formatter.locale()));
        }

        @Override
        public String toString() {
            return message(Locale.getDefault());
        }
    }

    /**
     * The 'user' resource bundle {@value #USER_BUNDLE}, as defined by the Jakarta Validation specification. This allows applications to override
     * messages (or to add other languages) by providing {@code ValidationMessages_<locale>.properties} in the root of their class path or module.
     */
    private static @Nullable ResourceBundle userBundle(Locale locale) {
        try {
            return withoutDefaultLocaleFallback(ResourceBundle.getBundle(USER_BUNDLE, locale), locale, () -> ResourceBundle.getBundle(USER_BUNDLE, Locale.ROOT));
        } catch (MissingResourceException mre) {
            logger.log(Level.FINER, mre.getMessage());
            return null;
        }
    }

    private static final ResourceBundle.Control CONTROL = ResourceBundle.Control.getControl(ResourceBundle.Control.FORMAT_DEFAULT);

    /**
     * If no bundle is found for the requested locale, {@link ResourceBundle#getBundle(String, Locale)} falls back to {@link Locale#getDefault()}
     * before falling back to the base bundle. That is not what we want if the requested locale is not the default locale: e.g. if English is requested,
     * and the default locale is Dutch, we want the (English) base bundle, not the Dutch one.
     * <p>
     * A {@link ResourceBundle.Control} without fallback would arrange that, but that is not supported in named modules. So we check the locale of the
     * found bundle, and use the base bundle if it is not one of the candidates for the requested locale.
     */
    static ResourceBundle withoutDefaultLocaleFallback(ResourceBundle found, Locale requested, java.util.function.Supplier<ResourceBundle> base) {
        if (CONTROL.getCandidateLocales("", requested).contains(found.getLocale())) {
            return found;
        }
        return base.get();
    }

    static final String USER_BUNDLE = "ValidationMessages";

    private static Optional<String> lookup(@Nullable ResourceBundle resourceBundle, String key) {
        try {
            if (resourceBundle != null && resourceBundle.containsKey(key)) {
                return Optional.of(resourceBundle.getString(key));
            }
        } catch (MissingResourceException mre) {
            logger.log(Level.FINE, mre.getMessage());
        }
        return Optional.empty();
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
