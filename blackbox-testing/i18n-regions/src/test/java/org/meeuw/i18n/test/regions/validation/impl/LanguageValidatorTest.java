package org.meeuw.i18n.test.regions.validation.impl;

import java.util.*;

import javax.validation.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.meeuw.i18n.regions.validation.Language;
import org.meeuw.i18n.regions.validation.RegionValidatorService;
import org.meeuw.i18n.regions.validation.impl.LanguageValidator;

import com.neovisionaries.i18n.LanguageAlpha3Code;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    private final LanguageValidator languageValidator = new LanguageValidator();

    @ParameterizedTest
    @ValueSource(strings = {"nl", "zxx", "jw", "dut"})
    public void testIsValid(String lang) {
        assertTrue(languageValidator.isValid(new Locale(lang), null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"cz"})
    public void testIsInValid(String lang) {
        assertFalse(languageValidator.isValid(new Locale(lang), null));
    }

    @Test
    public void nullIsValid() {
        assertTrue(languageValidator.isValid(null, null));

    }

    @Test
    @Disabled("fails. 'act' is somewhy not a known language")
    void achterhoeks() {
        assertTrue(languageValidator.isValid(new Locale("act"), null));
    }

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @ParameterizedTest
    @ValueSource(strings = {
        "ZZ",
        "nl-UU"// UU is not a valid country
    })
    public void testValidateInvalid(String language) {
        WithLanguageFields a = new WithLanguageFields();
        a.language = language;
        Set<ConstraintViolation<WithLanguageFields>> constraintViolations = testValidate(a, 1);
        assertThat(constraintViolations.iterator().next().getMessage()).isEqualTo(language + " is een ongeldige ISO639 taalcode");
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "ZZ",
        "nl-UU",// UU is not a valid country
        "nl-be"// case sensitive

    })
    public void testValidateInvalidNotForXml(String language) {
        WithLanguageFields a = new WithLanguageFields();
        a.notForXml = language;
        Set<ConstraintViolation<WithLanguageFields>> constraintViolations = testValidate(a, 1);
        assertThat(constraintViolations.iterator().next().getMessage()).isEqualTo(language + " is een ongeldige ISO639 taalcode");
    }
     @ParameterizedTest
    @ValueSource(strings = {
        "zh",
        "nl-NL-INFORMAL",// UU is not a valid country
        "nl-BE"// case sensitaive

    })
    public void testValidateValidNotForXml(String language) {
        WithLanguageFields a = new WithLanguageFields();
        a.notForXml = language;
        Set<ConstraintViolation<WithLanguageFields>> constraintViolations = testValidate(a, 0);
    }


    @Test
    public void testWithCountry() {
        {
            WithLanguageFields a = new WithLanguageFields();
            a.language = "nl-NL";
            testValidate(a, 0);
        }
        {
            WithLanguageFields a = new WithLanguageFields();
            a.languageNoCountry = "nl-NL";
            testValidate(a, 1);
        }
        {
            WithLanguageFields a = new WithLanguageFields();
            a.language = "nl-NL-INFORMAL";
            testValidate(a, 1);
        }
        {
            WithLanguageFields a = new WithLanguageFields();
            a.languageWithVariant = "nl-NL-INFORMAL";
            testValidate(a, 0);
        }

    }

    @Test
    void testObject() {
        {
            WithLanguageFields a = new WithLanguageFields();
            a.object = Arrays.asList("ZZ");
            testValidate(a, 1);
        }
        {
            WithLanguageFields a = new WithLanguageFields();
            a.object = Arrays.asList("nl-NL", "nl-be");
            testValidate(a, 0);
        }
    }

    private Set<ConstraintViolation<WithLanguageFields>> testValidate(WithLanguageFields value, int expectedSize) {
        Set<ConstraintViolation<WithLanguageFields>> validate = validator.validate(value);
        System.out.println("" + validate);
        assertThat(validate).hasSize(expectedSize);
        return validate;
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

    static class AXml {
        @Language(forXml = true)
        final String language;
        AXml(String l) {
            this.language = l;
        }
    }

    static class C {
        @Language(forXml = false)
        final String language;
        C(String l) {
                this.language = l;
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"nl", "nl-NL"})
    void validA(String lang) {
        assertThat(VALIDATOR.validate(new C(lang))).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"NL", "bl", "bl-A", "nl-A"})
    void invalidA(String lang) {
        assertThat(VALIDATOR.validate(new C(lang))).hasSize(1);
    }

}
