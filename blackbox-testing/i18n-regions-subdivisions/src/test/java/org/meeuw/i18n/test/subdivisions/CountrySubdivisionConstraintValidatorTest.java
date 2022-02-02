package org.meeuw.i18n.test.subdivisions;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.validation.*;

import org.junit.jupiter.api.Test;
import org.meeuw.i18n.countries.validation.ValidCountry;
import org.meeuw.i18n.regions.*;
import org.meeuw.i18n.regions.validation.RegionValidatorService;
import org.meeuw.i18n.subdivisions.validation.ValidCountrySubdivision;

import com.neovisionaries.i18n.LanguageCode;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 */
public class CountrySubdivisionConstraintValidatorTest {

    private static final ValidatorFactory FACTORY = Validation.buildDefaultValidatorFactory();
    private static final Validator VALIDATOR = FACTORY.getValidator();

    static class Netherlands {
        @ValidCountrySubdivision(country = @ValidCountry(codes = "NL"))
        public final Region region;

        public Netherlands(Region r) {
            this.region = r;
        }
    }
    static class Netherlandss {
        @ValidCountrySubdivision(country = @ValidCountry(codes = "NL"))
        public final List<Region> region;

        public Netherlandss(Region... r) {
            this.region = Arrays.asList(r);
        }
    }

    @Test
    public void includeValidCountries() {
        assertThat(RegionValidatorService.getInstance().fromProperty(Netherlands.class, "region").test("NL-DR")).isTrue();
        testAsStreamFilter(
            RegionValidatorService.getInstance().fromProperty(Netherlands.class, "region"),
            Netherlands::new, "NL-DR"
        );
    }

    @Test
    public void includeValidCountriess() {
        testAsStreamFilter(
            RegionValidatorService.getInstance().fromProperty(Netherlandss.class, "region"),
            Netherlandss::new, "NL-DR"
        );
    }

    @Test
    public void nullIsValid() {
        assertThat(VALIDATOR.validate(new Netherlands(null))).hasSize(0);
    }

    @Test
    public void convert() {
        class A {
            @ValidCountrySubdivision(country = @ValidCountry(codes = "NL"))
            public final String region;

            public A(String  r) {
                this.region = r;
            }
        }
        assertThat(VALIDATOR.validate(new A(null))).hasSize(0);
        assertThat(VALIDATOR.validate(new A("NL-AA"))).hasSize(0);
        assertThat(VALIDATOR.validate(new A("BE-AA"))).hasSize(1);
        assertThat(VALIDATOR.validate(new A("FOOBAR-AA"))).hasSize(1);

    }



    void testAsStreamFilter(
         Predicate<Object> predicate,
         Function<Region, Object> instantiator,
         String... assertToContain) {

        List<Region> validValues = RegionService.getInstance().values()
            .filter(predicate)
            .sorted(Regions.sortByName(LanguageCode.nl))
            .collect(Collectors.toList());

        for(Region r : validValues) {
            assertThat(VALIDATOR.validate(instantiator.apply(r))).withFailMessage(r + " was supposed to be valid, but is invalid").hasSize(0);
        }

        for (String code : assertToContain) {
            assertThat(validValues.stream().filter(r -> r.getCode().equals(code)).findFirst()).withFailMessage("No " + code + "found in " + validValues).isPresent();
        }
        List<Region> invalidValues = RegionService.getInstance().values()
            .filter(predicate.negate())
            .sorted(Regions.sortByName(LanguageCode.nl))
            .collect(Collectors.toList());
        for(Region r : invalidValues) {
            assertThat(VALIDATOR.validate(instantiator.apply(r)).size()).withFailMessage(""+ r + " is valid, but expected to be invalid").isGreaterThan(0);
        }
        System.out.println(validValues.stream()
            .map(r -> Regions.toStringWithCode(r, LanguageCode.nl))
            .collect(Collectors.joining("\n")));
    }

}
