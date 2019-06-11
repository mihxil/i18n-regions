package org.meeuw.i18n.validation;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.Test;
import org.meeuw.i18n.Country;
import org.meeuw.i18n.FormerlyAssignedCountryCode;
import org.meeuw.i18n.Region;
import com.neovisionaries.i18n.CountryCode;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
public class RegionValidatorTest {

    static class A {
        @ValidRegion
        Region region = Country.of(CountryCode.CS);
    }

    @Test
    public void isValid() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        A a = new A();
        assertThat(validator.validate(a)).hasSize(1);

        a.region = Country.of(FormerlyAssignedCountryCode.CSXX);
        assertThat(validator.validate(a)).hasSize(0);


    }
}
