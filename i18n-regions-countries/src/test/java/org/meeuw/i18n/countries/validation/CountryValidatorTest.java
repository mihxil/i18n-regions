package org.meeuw.i18n.countries.validation;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.Test;
import org.meeuw.i18n.Region;
import org.meeuw.i18n.RegionService;
import org.meeuw.i18n.Regions;
import org.meeuw.i18n.countries.FormerCountry;
import org.meeuw.i18n.countries.SomeRegion;
import org.meeuw.i18n.formerlyassigned.FormerlyAssignedCountryCode;
import org.meeuw.i18n.validation.RegionValidatorService;
import org.meeuw.i18n.validation.ValidRegion;

import com.neovisionaries.i18n.LanguageCode;

import static com.neovisionaries.i18n.CountryCode.CS;
import static com.neovisionaries.i18n.CountryCode.NL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.i18n.countries.Country.of;
import static org.meeuw.i18n.countries.UserAssignedCountry.ZZ;
import static org.meeuw.i18n.countries.validation.ValidCountry.FORMER;
import static org.meeuw.i18n.countries.validation.ValidCountry.OFFICIAL;
import static org.meeuw.i18n.formerlyassigned.FormerlyAssignedCountryCode.CSXX;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class CountryValidatorTest {



    static class CountryAndRegionsWithList {
        @ValidRegion(includes = {"GB-ENG", "GB-NIR", "GB-SCT", "GB-WLS"})
        @ValidCountry(value = OFFICIAL | FORMER)
        List<Region> region;

        public CountryAndRegionsWithList(Region... r) {
            this.region = Arrays.asList(r);
        }
    }
    static class FormerAndZZ {
        @ValidCountry(classes = {FormerCountry.class}, includes = "ZZ")
        List<Region> region;

        public FormerAndZZ(Region... r) {
            this.region = Arrays.asList(r);
        }
    }

    private static final ValidatorFactory FACTORY = Validation.buildDefaultValidatorFactory();
    private static final Validator VALIDATOR = FACTORY.getValidator();


    @Test
    public void combineValidRegionAndValidCountry() throws NoSuchFieldException {
        class CountryAndRegions {
            @ValidRegion(includes = {"GB-ENG", "GB-NIR", "GB-SCT", "GB-WLS"})
            @ValidCountry(value = OFFICIAL | FORMER)
            Region region;

            public CountryAndRegions(Region r) {
                this.region = r;
            }
        }
        {
            // CS is not an official country any more:
            assertThat(VALIDATOR.validate(new CountryAndRegions(of(CS)))).hasSize(1);
            assertThat(VALIDATOR.validate(new CountryAndRegions(of(CS))).iterator().next().getMessage()).isEqualTo("CS is not a valid country");;
        }

        {
            // It's now the former country 'CSXX':
            assertThat(VALIDATOR.validate(new CountryAndRegions(of(CSXX)))).hasSize(0);
        }
        {
            // an official country
            assertThat(VALIDATOR.validate(new CountryAndRegions(of(NL)))).hasSize(0);
        }
        {
            // ZZ is a user assigned country, so invalid
            assertThat(VALIDATOR.validate(new CountryAndRegions(ZZ))).hasSize(1);
        }
        assertThat(VALIDATOR.validate(new CountryAndRegions(new SomeRegion("GB-ENG")))).hasSize(0);
        //assertThat(VALIDATOR.validate(new A(CountrySubdivision.of(GB, "NIR").orElse(null)))).hasSize(0);

        testAsStreamFilter(
            RegionValidatorService.fromProperty(CountryAndRegions.class, "region"),
            CountryAndRegions::new, "TPTL", "GB-ENG");

    }

    @Test
    public void combineValidRegionAndValidCountryString() throws NoSuchFieldException {

        class CountryAndRegionsAsString {
            @ValidRegion(includes = {"GB-ENG", "GB-NIR", "GB-SCT", "GB-WLS"})
            @ValidCountry(value = OFFICIAL | FORMER)
            String region;

            public CountryAndRegionsAsString(String r) {
                this.region = r;
            }
            public CountryAndRegionsAsString(Region r) {
                this(r.getCode());
            }
        }
        {
            // CS is not an official country any more:
            assertThat(VALIDATOR.validate(new CountryAndRegionsAsString(of(CS)))).hasSize(1);
            assertThat(VALIDATOR.validate(new CountryAndRegionsAsString(of(CS))).iterator().next().getMessage()).isEqualTo("CS is not a valid country");;
        }

        {
            // It's now the former country 'CSXX':
            assertThat(VALIDATOR.validate(new CountryAndRegionsAsString(of(CSXX)))).hasSize(0);
        }
        {
            // an official country
            assertThat(VALIDATOR.validate(new CountryAndRegionsAsString(of(NL)))).hasSize(0);
        }
        {
            // ZZ is a user assigned country, so invalid
            assertThat(VALIDATOR.validate(new CountryAndRegionsAsString(ZZ))).hasSize(1);
        }
        assertThat(VALIDATOR.validate(new CountryAndRegionsAsString(new SomeRegion("GB-ENG")))).hasSize(0);
        //assertThat(VALIDATOR.validate(new A(CountrySubdivision.of(GB, "NIR").orElse(null)))).hasSize(0);

        testAsStreamFilter(
            RegionValidatorService.fromProperty(CountryAndRegionsAsString.class, "region"),
            CountryAndRegionsAsString::new, "TPTL", "GB-ENG");
    }



    @Test
    public void isValidWithList() {

        assertThat(VALIDATOR.validate(new CountryAndRegionsWithList(of(NL), of(FormerlyAssignedCountryCode.TPTL)))).hasSize(0);


    }

    @Test
    public void onlyIncludes() {


        assertThat(VALIDATOR.validate(new FormerAndZZ(of(CS)))).hasSize(1);

        assertThat(VALIDATOR.validate(new FormerAndZZ(of(CSXX)))).hasSize(0);

        assertThat(VALIDATOR.validate(new FormerAndZZ(of(NL)))).hasSize(1);

        assertThat(VALIDATOR.validate(new FormerAndZZ(ZZ))).hasSize(0);
    }



    @Test
    public void testClasses() {


        assertThat(VALIDATOR.validate(new FormerAndZZ(of(CS)))).hasSize(1);

        assertThat(VALIDATOR.validate(new FormerAndZZ(of(CSXX)))).hasSize(0);

        assertThat(VALIDATOR.validate(new FormerAndZZ(of(NL)))).hasSize(1);

        assertThat(VALIDATOR.validate(new FormerAndZZ(ZZ))).hasSize(0);
    }

    public void testAsStreamFilter(
        Predicate<Object> predicate,
        Function<Region, Object> instantiator,
        String... assertToContain) throws NoSuchFieldException {

        List<Region> validValues = RegionService.getInstance().values()
            .filter(predicate)
            .sorted(Regions.sortByName(LanguageCode.nl))
            .collect(Collectors.toList());
        for(Region r : validValues) {
            assertThat(VALIDATOR.validate(instantiator.apply(r))).hasSize(0);
        }
        for (String code : assertToContain) {
            assertThat(validValues.stream().filter(r -> r.getCode().equals(code)).findFirst()).isPresent();
        }
        List<Region> invalidValues = RegionService.getInstance().values()
            .filter(predicate.negate())
            .sorted(Regions.sortByName(LanguageCode.nl))
            .collect(Collectors.toList());
        for(Region r : invalidValues) {
            assertThat(VALIDATOR.validate(instantiator.apply(r)).size()).isGreaterThan(0);
        }
        System.out.println(validValues.stream()
            .map(r -> Regions.toStringWithCode(r, LanguageCode.nl))
            .collect(Collectors.joining("\n")));
    }


}
