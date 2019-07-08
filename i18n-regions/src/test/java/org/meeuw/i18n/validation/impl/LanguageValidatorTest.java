package org.meeuw.i18n.validation.impl;

import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.Ignore;
import org.junit.Test;
import org.meeuw.i18n.validation.Language;
import org.meeuw.i18n.validation.RegionValidatorService;

import com.neovisionaries.i18n.LanguageAlpha3Code;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Michiel Meeuwissen
 * @since 0.3
 */
public class LanguageValidatorTest {
    private static final RegionValidatorService regionValidatorService = RegionValidatorService.getInstance();
    private static final Validator VALIDATOR = regionValidatorService.getValidator();

    static {
        Locale.setDefault(new Locale("nl"));
    }

    LanguageValidator validator = new LanguageValidator();

    @Test
    public void testIsValid() {
        assertTrue(validator.isValid(new Locale("nl"), null));

    }

    @Test
    public void testIsValidCz() {
        assertFalse(validator.isValid(new Locale("cz"), null));

    }

    @Test
    public void testIsValidZxx() {
        assertTrue(validator.isValid(new Locale("zxx"), null));

    }

    @Test
    public void testIsValidJw() {
        assertTrue(validator.isValid(new Locale("jw"), null));

    }

    @Test
    public void iso3() {
        assertTrue(validator.isValid(new Locale("dut"), null));

    }

    @Test
    @Ignore("fails")
    public void achterhoeks() {
        assertTrue(validator.isValid(new Locale("act"), null));

    }

    public static class A {
        @Language
        String language;
    }

    @Test
    public void testZZ() {
        A a = new A();
        a.language = "ZZ";
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<A>> validate = validator.validate(a);
        System.out.println("" + validate);

    }

    @Test
    @Ignore
    public void wiki() {
        Map<String, String> result = new TreeMap<>();
        for (String s : Locale.getISOLanguages()) {
            result.put(s, new Locale(s).getDisplayLanguage(new Locale("en")));
        }
        for (LanguageAlpha3Code s : LanguageAlpha3Code.values()) {
            result.put(s.toString(), s.getName());
        }
        // output sorted
        System.out.println("||code||name in english||name in dutch||name in language itself||");
        for (Map.Entry<String, String> e : result.entrySet()) {
            assertTrue(validator.isValid(new Locale(e.getKey()), null));
            String en = e.getValue();
            String nl = new Locale(e.getKey()).getDisplayLanguage(new Locale("nl"));
            if (nl.equals(en) || nl.equals(e.getKey())) {
                nl = " ";
            }
            String self = new Locale(e.getKey()).getDisplayLanguage(new Locale(e.getKey()));
            if (self.equals(en) || self.equals(e.getKey())) {
                self = " ";
            }
            System.out.println("|" + e.getKey() + "|" + en + "|"
                + nl + "|" + self + "|");
        }
    }

    @Test
    public void test() {

        class A {
            @Language
            String language;
            A(String l) {
                this.language = l;
            }
        }

        assertThat(VALIDATOR.validate(new A("nl"))).isEmpty();

    }

}
