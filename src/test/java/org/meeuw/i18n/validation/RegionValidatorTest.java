package org.meeuw.i18n.validation;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.Test;
import org.meeuw.i18n.*;

import static com.neovisionaries.i18n.CountryCode.CS;
import static com.neovisionaries.i18n.CountryCode.NL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.i18n.Country.of;
import static org.meeuw.i18n.FormerlyAssignedCountryCode.CSXX;
import static org.meeuw.i18n.UserAssignedCountry.ZZ;
import static org.meeuw.i18n.validation.ValidRegion.FORMER;
import static org.meeuw.i18n.validation.ValidRegion.OFFICIAL;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class RegionValidatorTest {

    static class A {
        @ValidRegion(predicates = OFFICIAL | FORMER)
        Region region;

        public A(Region r) {
            this.region = r;
        }
    }

    @Test
    public void isValid() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        assertThat(validator.validate(new A(of(CS)))).hasSize(1);

        assertThat(validator.validate(new A(of(CSXX)))).hasSize(0);

        assertThat(validator.validate(new A(of(NL)))).hasSize(0);


        assertThat(validator.validate(new A(ZZ))).hasSize(1);

    }
}
