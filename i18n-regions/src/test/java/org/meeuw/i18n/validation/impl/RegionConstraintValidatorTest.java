package org.meeuw.i18n.validation.impl;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Validator;

import org.junit.Test;
import org.meeuw.i18n.Region;
import org.meeuw.i18n.RegionService;
import org.meeuw.i18n.validation.RegionValidatorService;
import org.meeuw.i18n.validation.ValidRegion;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
public class RegionConstraintValidatorTest {
     private static final RegionValidatorService regionValidatorService = RegionValidatorService.getInstance();
    private static final Validator VALIDATOR = regionValidatorService.getValidator();

    static {
        Locale.setDefault(new Locale("nl"));
    }


    @Test
    public void validateLocale() {

        class A {
            @ValidRegion
            Locale locale;

            public A(Locale locale) {
                this.locale = locale;
            }
        }
        A invalidCountry = new A(new Locale("nl", "XX"));
        assertThat(VALIDATOR.validate(invalidCountry)).hasSize(1);

        A noCountry = new A(new Locale("nl"));
        assertThat(VALIDATOR.validate(noCountry)).hasSize(0);

        A validCountry = new A(new Locale("nl", "NL"));
        assertThat(VALIDATOR.validate(validCountry)).hasSize(0);


    }

    @Test
    public void validateByType() {

        class A {
            @ValidRegion(types = Region.Type.COUNTRY)
            Region region;

            public A(Region region) {
                this.region = region;
            }
        }
        A valid = new A(RegionService.getInstance().getByCode("NL").orElseThrow(IllegalStateException::new));
        assertThat(VALIDATOR.validate(valid)).hasSize(0);


         class B  {
            @ValidRegion(types = Region.Type.CONTINENT)
            Region region;

            public B(Region region) {
                this.region = region;
            }
        }
        B invalid = new B(RegionService.getInstance().getByCode("NL").orElseThrow(IllegalStateException::new));
        assertThat(VALIDATOR.validate(invalid)).hasSize(1);

    }

    @Test
    public void listProperty() {

        class A {
            List<@ValidRegion(types = Region.Type.COUNTRY)  Region> regions;
        }
        class B  {
            List<@ValidRegion(excludes = "UK")  Region> regions;
        }

        Set<String> regions = RegionService.getInstance().values().filter(RegionValidatorService.getInstance().fromListProperty(A.class, "regions")).map(Region::getCode).collect(Collectors.toSet());
        assertThat(regions).containsExactly("UK", "NL");

         Set<String> ukExcludes = RegionService.getInstance().values().filter(RegionValidatorService.getInstance().fromListProperty(B.class, "regions")).map(Region::getCode).collect(Collectors.toSet());
        assertThat(ukExcludes).containsExactly("NL");


    }

    @Test
    public void setProperty() {

        class A {
            Set<@ValidRegion(types = Region.Type.COUNTRY)  Region> regions;
        }
        class B  {
            Set<@ValidRegion(excludes = "UK")  Region> regions;
        }

        Set<String> regions = RegionService.getInstance().values().filter(RegionValidatorService.getInstance().fromListProperty(A.class, "regions")).map(Region::getCode).collect(Collectors.toSet());
        assertThat(regions).containsExactly("UK", "NL");

         Set<String> ukExcludes = RegionService.getInstance().values().filter(RegionValidatorService.getInstance().fromListProperty(B.class, "regions")).map(Region::getCode).collect(Collectors.toSet());
        assertThat(ukExcludes).containsExactly("NL");


    }

}
