package org.meeuw.i18n.test.regions.validation.impl;

import java.util.*;
import java.util.stream.Collectors;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import org.junit.jupiter.api.Test;
import org.meeuw.i18n.regions.Region;
import org.meeuw.i18n.regions.validation.RegionValidatorService;
import org.meeuw.i18n.regions.validation.ValidRegion;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.i18n.regions.RegionService.getInstance;

/**
 * @author Michiel Meeuwissen
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
            final Locale locale;

            A(Locale locale) {
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
    void validateByType() {

        class A {
            @ValidRegion(types = Region.Type.COUNTRY)
            final Region region;

            A(Region region) {
                this.region = region;
            }
        }
        A valid = new A(getInstance().getByCode("NL").orElseThrow(IllegalStateException::new));
        assertThat(VALIDATOR.validate(valid)).hasSize(0);


         class B  {
            @ValidRegion(types = Region.Type.CONTINENT)
            final Region region;

            B(Region region) {
                this.region = region;
            }
        }
        {
            B invalid = new B(getInstance().getByCode("NL").orElseThrow(IllegalStateException::new));
            assertThat(VALIDATOR.validate(invalid)).hasSize(1);
        }
        {
            B nullIsValid = new B(null);
            assertThat(VALIDATOR.validate(nullIsValid)).hasSize(0);

        }


    }

    @Test
    public void validateObject() {

        class A {
            @ValidRegion(types = Region.Type.COUNTRY)
            final Object region;

            A(Object region) {
                this.region = region;
            }
        }
        {
            A listWithValid = new A(Arrays.asList(getInstance().getByCode("NL").orElseThrow(IllegalStateException::new)));
            assertThat(VALIDATOR.validate(listWithValid)).hasSize(0);
        }

        {
            A listWithInValid = new A(Arrays.asList("BLA", "NL"));
            Set<ConstraintViolation<A>> validate = VALIDATOR.validate(listWithInValid);
            System.out.println("" + validate);
            assertThat(VALIDATOR.validate(listWithInValid)).hasSize(1);
        }
        {
            A withUnknownType = new A(1);
            Set<ConstraintViolation<A>> validate = VALIDATOR.validate(withUnknownType);
            System.out.println("" + validate);
            assertThat(VALIDATOR.validate(withUnknownType)).hasSize(1);
        }

    }

    @Test
    void listProperty() {

        class A {
            List<@ValidRegion(types = Region.Type.COUNTRY)  Region> regions;
        }
        class B  {
            List<@ValidRegion(excludes = "UK")  Region> regions;
        }

        Set<String> regions = getInstance().values()
            .filter(regionValidatorService.fromListProperty(A.class, "regions"))
            .map(Region::getCode)
            .collect(Collectors.toSet());
        assertThat(regions).contains("BE", "UK", "NL");

         Set<String> ukExcludes = getInstance().values()
             .filter(regionValidatorService.fromListProperty(B.class, "regions"))
             .map(Region::getCode)
             .collect(Collectors.toSet());
        assertThat(ukExcludes).doesNotContain("UK");


    }

    @Test
    void setProperty() {

        class A {
            Set<@ValidRegion(types = Region.Type.COUNTRY)  Region> regions;
        }
        class B  {
            Set<@ValidRegion(excludes = "UK")  Region> regions;
        }

        Set<String> regions = getInstance().values()
            .filter(regionValidatorService.fromListProperty(A.class, "regions"))
            .map(Region::getCode)
            .collect(Collectors.toSet());
        assertThat(regions).containsExactly("BE", "UK", "NL");

         Set<String> ukExcludes = getInstance().values()
             .filter(regionValidatorService.fromListProperty(B.class, "regions"))
             .map(Region::getCode)
             .collect(Collectors.toSet());
        assertThat(ukExcludes).containsExactly("BE", "NL");


    }


    @Test
    void codes() {
        class A {
            List<@ValidRegion(codes = {"NL", "BE"}, excludes = {"BE"}) String> regions;
        }
        Set<String> list = getInstance().values()
            .filter(regionValidatorService.fromListProperty(A.class, "regions")).map(Region::getCode).collect(Collectors.toSet());

        assertThat(list).containsExactly("NL");


    }

}
