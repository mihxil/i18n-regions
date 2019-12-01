package org.meeuw.i18n.it.regions.validation.impl;

import java.util.*;

import javax.validation.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.meeuw.i18n.regions.validation.Language;
import org.meeuw.i18n.regions.validation.RegionValidatorService;
import org.meeuw.i18n.regions.validation.impl.LanguageValidator;

import com.neovisionaries.i18n.LanguageAlpha3Code;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

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

    private LanguageValidator languageValidator = new LanguageValidator();

    @Test
    public void testIsValid() {
        assertTrue(languageValidator.isValid(new Locale("nl"), null));

    }

    @Test
    public void testIsValidCz() {
        assertFalse(languageValidator.isValid(new Locale("cz"), null));

    }

    @Test
    public void testIsValidZxx() {
        assertTrue(languageValidator.isValid(new Locale("zxx"), null));

    }

    @Test
    void testIsValidJw() {
        assertTrue(languageValidator.isValid(new Locale("jw"), null));

    }

    @Test
    void iso3() {
        assertTrue(languageValidator.isValid(new Locale("dut"), null));

    }

    @Test
    @Disabled("fails. 'act' is somewhy not a known language")
    void achterhoeks() {
        assertTrue(languageValidator.isValid(new Locale("act"), null));

    }

    private ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private Validator validator = factory.getValidator();

    @Test
    public void testZZ() {
        A a = new A();
        a.language = "ZZ";
        testValidate(a, 1);
    }


    @Test
    public void testWithCountry() {
        {
            A a = new A();
            a.language = "nl-NL";
            testValidate(a, 0);
        }
          {
            A a = new A();
            a.language = "nl-UU"; // UU is not a valid country
            testValidate(a, 1);
        }
        {
            A a = new A();
            a.languageNoCountry = "nl-NL";
            testValidate(a, 1);
        }
        {
            A a = new A();
            a.language = "nl-NL-INFORMAL";
            testValidate(a, 1);
        }
        {
            A a = new A();
            a.languageWithVariant = "nl-NL-INFORMAL";
            testValidate(a, 0);
        }

    }

    @Test
    void testObject() {
        {
            A a = new A();
            a.object = Arrays.asList("ZZ");
            testValidate(a, 1);
        }
        {
            A a = new A();
            a.object = Arrays.asList("nl-NL", "nl-BE");
            testValidate(a, 0);
        }
    }

    private void testValidate(A value, int expectedSize) {
        Set<ConstraintViolation<A>> validate = validator.validate(value);
        System.out.println("" + validate);
        assertThat(validate).hasSize(expectedSize);
    }


    @Test
    @Disabled
    void wiki() {
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
            assertTrue(languageValidator.isValid(new Locale(e.getKey()), null));
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
    void test() {

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
