package org.meeuw.i18n.test.countries;

import java.util.Locale;

import jakarta.validation.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Isolated;
import org.meeuw.i18n.countries.Country;
import org.meeuw.i18n.countries.validation.ValidCountry;
import org.meeuw.i18n.regions.Region;
import org.meeuw.i18n.regions.validation.ValidRegion;
import org.meeuw.i18n.test.some.SomeRegion;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.i18n.countries.codes.CountryCode.CS;

/**
 * The messages must be in the locale of the message interpolator, which is not necessarily {@link Locale#getDefault()}. E.g. spring's
 * {@code LocaleContextMessageInterpolator} interpolates in the locale of the current request.
 */
@Isolated
class ValidationMessageLocaleTest {

    static class A {
        @ValidCountry
        final Region region;

        A(Region region) {
            this.region = region;
        }
    }

    static class B {
        @ValidRegion(classes = Country.class)
        final Region region;

        B(Region region) {
            this.region = region;
        }
    }

    @Test
    void interpolatorLocaleDiffersFromDefault() {
        assertThat(message(Locale.ENGLISH, new Locale("nl"), new A(Country.of(CS)))).isEqualTo("CS is geen geldig land");
        assertThat(message(Locale.ENGLISH, new Locale("nl"), new B(new SomeRegion("bla")))).isEqualTo("bla is geen geldig gebied");

        assertThat(message(new Locale("nl"), Locale.ENGLISH, new A(Country.of(CS)))).isEqualTo("CS is not a valid country");
        assertThat(message(new Locale("nl"), Locale.ENGLISH, new B(new SomeRegion("bla")))).isEqualTo("bla is not a valid region");
    }

    /**
     * Overrides in the 'user' bundle {@code ValidationMessages} are taken in the locale of the interpolator too.
     */
    @Test
    void overrideInInterpolatorLocale() {
        assertThat(message(Locale.ENGLISH, new Locale("fy"), new A(Country.of(CS)))).isEqualTo("CS is gjin jildich l\u00E2n");
    }

    /**
     * The validated value must not be interpreted as an expression or a message parameter.
     */
    @Test
    void valueIsNotInterpreted() {
        assertThat(message(Locale.ENGLISH, new Locale("nl"), new B(new SomeRegion("${1+1}")))).isEqualTo("${1+1} is geen geldig gebied");
        assertThat(message(Locale.ENGLISH, new Locale("nl"), new B(new SomeRegion("{jakarta.validation.constraints.Min.message}")))).isEqualTo("{jakarta.validation.constraints.Min.message} is geen geldig gebied");
    }

    /**
     * @param defaultLocale     The JVM default locale while validating
     * @param interpolateLocale The locale the message interpolator is asked to use
     */
    static String message(Locale defaultLocale, Locale interpolateLocale, Object bean) {
        Locale before = Locale.getDefault();
        try {
            Locale.setDefault(defaultLocale);
            Configuration<?> configuration = Validation.byDefaultProvider().configure();
            MessageInterpolator delegate = configuration.getDefaultMessageInterpolator();
            configuration.messageInterpolator(new FixedLocaleMessageInterpolator(delegate, interpolateLocale));
            try (ValidatorFactory factory = configuration.buildValidatorFactory()) {
                return factory.getValidator().validate(bean)
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .findFirst()
                    .orElseThrow();
            }
        } finally {
            Locale.setDefault(before);
        }
    }

    record FixedLocaleMessageInterpolator(MessageInterpolator delegate, Locale locale) implements MessageInterpolator {
        @Override
        public String interpolate(String messageTemplate, Context context) {
            return delegate.interpolate(messageTemplate, context, locale);
        }

        @Override
        public String interpolate(String messageTemplate, Context context, Locale locale) {
            return delegate.interpolate(messageTemplate, context, locale);
        }
    }
}
