package org.meeuw.i18n.test.regions.validation.impl;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ValidationException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Isolated;
import org.meeuw.i18n.regions.validation.ValidRegion;
import org.meeuw.i18n.regions.validation.impl.RegionConstraintValidator;
import org.meeuw.i18n.regions.validation.impl.ValidationMessages;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the fall back of {@link ValidationMessages} for validation providers other than hibernate validator: then the message is resolved in
 * {@link Locale#getDefault()}.
 */
@Isolated
class ValidationMessagesTest {

    @ValidRegion
    static Object annotated;

    @Test
    void otherProviderResolvesInDefaultLocale() {
        assertThat(templateFor(new Locale("nl"), "bla")).containsExactly("bla is geen geldig gebied");
        assertThat(templateFor(Locale.ENGLISH, "bla")).containsExactly("bla is not a valid region");
    }

    @Test
    void otherProviderEscapesValue() {
        assertThat(templateFor(Locale.ENGLISH, "${x}{y}")).containsExactly("\\$\\{x\\}\\{y\\} is not a valid region");
    }

    /**
     * @return the templates of the constraint violations added by {@link RegionConstraintValidator} (via {@link ValidationMessages#resolveDefaultMessage})
     */
    static List<String> templateFor(Locale defaultLocale, Object value) {
        Locale before = Locale.getDefault();
        try {
            Locale.setDefault(defaultLocale);
            List<String> templates = new ArrayList<>();
            RegionConstraintValidator validator = new RegionConstraintValidator();
            validator.initialize(ValidationMessagesTest.class.getDeclaredField("annotated").getAnnotation(ValidRegion.class));
            assertThat(validator.isValid(value, context("{org.meeuw.i18n.regions.validation.region.message}", templates))).isFalse();
            return templates;
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException(e);
        } finally {
            Locale.setDefault(before);
        }
    }

    /**
     * A {@link ConstraintValidatorContext} which is not hibernate validator's (so {@link ConstraintValidatorContext#unwrap(Class)} fails), and records
     * the templates of the added violations.
     */
    static ConstraintValidatorContext context(String defaultTemplate, List<String> templates) {
        return (ConstraintValidatorContext) Proxy.newProxyInstance(
            ValidationMessagesTest.class.getClassLoader(),
            new Class<?>[]{ConstraintValidatorContext.class},
            (proxy, method, args) -> switch (method.getName()) {
                case "getDefaultConstraintMessageTemplate" -> defaultTemplate;
                case "disableDefaultConstraintViolation" -> null;
                case "unwrap" -> throw new ValidationException("not supported");
                case "buildConstraintViolationWithTemplate" -> {
                    templates.add((String) args[0]);
                    yield Proxy.newProxyInstance(
                        ValidationMessagesTest.class.getClassLoader(),
                        new Class<?>[]{ConstraintValidatorContext.ConstraintViolationBuilder.class},
                        (p, m, a) -> proxy);
                }
                default -> throw new UnsupportedOperationException(method.getName());
            });
    }
}
