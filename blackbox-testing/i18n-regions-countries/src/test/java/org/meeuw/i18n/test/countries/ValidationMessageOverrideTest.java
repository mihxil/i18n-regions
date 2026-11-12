package org.meeuw.i18n.test.countries;

import java.util.Locale;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.Min;

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
 * Tests whether the validation messages can be overridden by providing {@code ValidationMessages_<locale>.properties} resource bundles
 * in the root of the class path (or module), like is usual with Jakarta Validation. This works per key, so messages which are not overridden
 * are still taken from the bundles of i18n-regions itself.
 * <p>
 * To show that this is consistent with how the validation provider itself treats messages, a message of a standard constraint ({@link Min})
 * is overridden too.
 * <p>
 * Similar to {@link DisplayNameOverrideTest}.
 */
@Isolated
class ValidationMessageOverrideTest {

    static final Locale DUTCH = new Locale("nl");
    static final Locale DUTCH_CUSTOM = new Locale("nl", "NL", "custom");
    static final Locale FRISIAN = new Locale("fy");

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

    static class C {
        @Min(10)
        final int number;

        C(int number) {
            this.number = number;
        }
    }

    @Test
    void dutch() {
        assertThat(messageIn(DUTCH)).isEqualTo("CS is geen geldig land");
        assertThat(messageIn(DUTCH, new C(5))).isEqualTo("moet groter of gelijk aan 10 zijn"); // supplied by hibernate validator itself
    }

    @Test
    void dutchVariant() {
        assertThat(messageIn(DUTCH_CUSTOM)).isEqualTo("CS is geen geldig land (aangepast)");
        // not overridden, so falls back to the Dutch message of i18n-regions itself
        assertThat(messageIn(DUTCH_CUSTOM, new B(new SomeRegion("bla")))).isEqualTo("bla is geen geldig gebied");

        assertThat(messageIn(DUTCH_CUSTOM, new C(5))).isEqualTo("moet minstens 10 zijn (aangepast)");
    }

    @Test
    void frisian() {
        assertThat(messageIn(FRISIAN)).isEqualTo("CS is gjin jildich lân");
        assertThat(messageIn(FRISIAN, new C(5))).isEqualTo("moat op syn minst 10 wêze");
    }

    static String messageIn(Locale locale) {
        return messageIn(locale, new A(Country.of(CS)));
    }

    static String messageIn(Locale locale, Object bean) {
        Locale defaultLocale = Locale.getDefault();
        try {
            Locale.setDefault(locale);
            // the validation provider determines its locale when the factory is created
            try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
                return factory.getValidator().validate(bean)
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .findFirst()
                    .orElseThrow();
            }
        } finally {
            Locale.setDefault(defaultLocale);
        }
    }
}
