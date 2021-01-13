package org.meeuw.i18n.test.countries;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;

import org.junit.jupiter.api.Test;
import org.meeuw.i18n.countries.*;
import org.meeuw.i18n.countries.validation.ValidCountry;
import org.meeuw.i18n.formerlyassigned.FormerlyAssignedCountryCode;
import org.meeuw.i18n.regions.*;
import org.meeuw.i18n.regions.validation.RegionValidatorService;
import org.meeuw.i18n.regions.validation.ValidRegion;
import org.meeuw.i18n.test.some.SomeRegion;

import com.neovisionaries.i18n.LanguageCode;

import static com.neovisionaries.i18n.CountryCode.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class CountryValidatorTest {

    private static final RegionValidatorService regionValidatorService = RegionValidatorService.getInstance();
    private static final Validator VALIDATOR = regionValidatorService.getValidator();

    static {
        Locale.setDefault(new Locale("nl"));
    }


    public static class CountryAndRegions {
        @ValidRegion(includes = {"GB-ENG", "GB-NIR", "GB-SCT", "GB-WLS"})
        @ValidCountry(value = ValidCountry.OFFICIAL | ValidCountry.FORMER)
        public final Region region;

        public CountryAndRegions(Region r) {
            this.region = r;
        }
    }
    public static class CountryAndRegionsOrUserDefined {

        @ValidRegion(includes = {"GB-ENG", "GB-NIR", "GB-SCT", "GB-WLS"})
        @ValidCountry(value = ValidCountry.OFFICIAL | ValidCountry.FORMER | ValidCountry.USER_ASSIGNED)
        public final Region orUsedDefined;


        public CountryAndRegionsOrUserDefined(Region r) {
            this.orUsedDefined = r;
        }
    }


    @Test
    public void combineValidRegionAndValidCountry() throws NoSuchFieldException {

        {
            // CS is not an official country any more:
            assertThat(VALIDATOR.validate(new CountryAndRegions(Country.of(CS)))).hasSize(1);
            assertThat(VALIDATOR.validate(new CountryAndRegions(Country.of(CS))).iterator().next().getMessage()).isEqualTo("CS is not a valid country");
        }

        {
            // It's now the former country 'CSXX':
            assertThat(VALIDATOR.validate(new CountryAndRegions(Country.of(FormerlyAssignedCountryCode.CSXX)))).hasSize(0);
        }
        {
            // an official country
            assertThat(VALIDATOR.validate(new CountryAndRegions(Country.of(NL)))).hasSize(0);
        }
        {
            // ZZ is a user assigned country, so invalid
            assertThat(VALIDATOR.validate(new CountryAndRegions(UserAssignedCountry.ZZ))).hasSize(1);
        }
        //assertThat(VALIDATOR.validate(new CountryAndRegions(new SomeRegion("GB-ENG")))).hasSize(0);
        //assertThat(VALIDATOR.validate(new A(CountrySubdivision.of(GB, "NIR").orElse(null)))).hasSize(0);

        testAsStreamFilter(
            regionValidatorService.fromProperty(CountryAndRegions.class, "region"),
            CountryAndRegions::new, "TPTL", "GB-ENG");

    }

    public static class CountryAndRegionsAsString {
        @ValidRegion(includes = {"GB-ENG", "GB-NIR", "GB-SCT", "GB-WLS"})
        @ValidCountry(value = ValidCountry.OFFICIAL | ValidCountry.FORMER)
        public final String region;

        CountryAndRegionsAsString(String r) {
            this.region = r;
        }
        CountryAndRegionsAsString(Region r) {
            this(r.getCode());
        }
    }
    @Test
    public void combineValidRegionAndValidCountryString() throws NoSuchFieldException {


        {
            // CS is not an official country any more:
            assertThat(VALIDATOR.validate(new CountryAndRegionsAsString(Country.of(CS)))).hasSize(1);
            assertThat(VALIDATOR.validate(new CountryAndRegionsAsString(Country.of(CS))).iterator().next().getMessage()).isEqualTo("CS is not a valid country");
        }

        {
            // It's now the former country 'CSXX':
            assertThat(VALIDATOR.validate(new CountryAndRegionsAsString(Country.of(FormerlyAssignedCountryCode.CSXX)))).hasSize(0);
        }
        {
            // an official country
            assertThat(VALIDATOR.validate(new CountryAndRegionsAsString(Country.of(NL)))).hasSize(0);
        }
        {
            // ZZ is a user assigned country, so invalid
            assertThat(VALIDATOR.validate(new CountryAndRegionsAsString(UserAssignedCountry.ZZ))).hasSize(1);
        }
        //assertThat(VALIDATOR.validate(new CountryAndRegionsAsString(new SomeRegion("GB-ENG")))).hasSize(0);
        //assertThat(VALIDATOR.validate(new A(CountrySubdivision.of(GB, "NIR").orElse(null)))).hasSize(0);

        testAsStreamFilter(
            regionValidatorService.fromProperty(CountryAndRegionsAsString.class, "region"),
            CountryAndRegionsAsString::new, "TPTL", "GB-ENG");
    }

   public static class CountryAndRegionsWithList {
       public final List<
           // valid are countries (further validated by @ValidCountry), and a list of codes.
           @ValidRegion(includes = {"GB-ENG", "GB-NIR", "GB-SCT", "GB-WLS"}, classes= {Country.class}, payload = {})
           @ValidCountry(value = ValidCountry.OFFICIAL | ValidCountry.FORMER, includes = "CS")
           @NotNull
               Region> region;

       CountryAndRegionsWithList(Region... r) {
           this.region = Arrays.asList(r);
       }
   }

    @Test
    public void isValidWithList() {

        Set<ConstraintViolation<CountryAndRegionsWithList>> violations= VALIDATOR.validate(new CountryAndRegionsWithList(
            Country.of(NL),
            Country.of(FormerlyAssignedCountryCode.TPTL),
            new SomeRegion("bla"),
            Country.of(CS),
            Country.of(DG),
            null
        ));
        assertThat(violations.stream().map(ConstraintViolation::getMessage))
            .containsExactlyInAnyOrder("bla is not a valid region", "must not be null", "DG is not a valid country");


    }


    public static class CountryAndRegionsWithList2 {

        @ValidRegion(includes = {"GB-ENG", "GB-NIR", "GB-SCT", "GB-WLS"})
        @ValidCountry(value = ValidCountry.OFFICIAL | ValidCountry.FORMER, includes = "CS")
        public final List<Region> region;

        CountryAndRegionsWithList2(Region... r) {
            this.region = Arrays.asList(r);
        }
    }
    @Test
    public void isValidWithList2() {

        assertThat(VALIDATOR.validate(new CountryAndRegionsWithList2(
            Country.of(NL),
            Country.of(FormerlyAssignedCountryCode.TPTL),
            Country.of(CS)
            ))
        ).hasSize(0);


    }

    @Test
    public void onlyIncludes() throws NoSuchFieldException {
        class ZZ {
            public List<Country> region;

            ZZ(Region r) {
                this.region = r instanceof  Country ? Collections.singletonList((Country) r) : null;
            }
            public void setRegion(List<Country> regions) {
                this.region = regions;
            }
            @ValidRegion(classes = Country.class)
            @ValidCountry(value = 0, includes = "ZZ")
            @NotNull
            public List<Country> getRegion() {
                return this.region;
            }

        }


        assertThat(VALIDATOR.validate(new ZZ(Country.of(NL)))).hasSize(1);

        assertThat(VALIDATOR.validate(new ZZ(UserAssignedCountry.ZZ))).hasSize(0);
        testAsStreamFilter(
            regionValidatorService.fromProperty(ZZ.class, "region"),
            ZZ::new);
    }




    @Test
    public void testClasses() throws NoSuchFieldException {
        class Former {
            @ValidRegion(classes = {FormerCountry.class})
            @NotNull
            List<Country> region;

            Former(Country... r) {
                this.region = Arrays.asList(r);
            }
            Former(Region r) {
                this.region = r instanceof Country ? Collections.singletonList((Country) r) : null;
            }
            public void setRegion(List<Country> regions) {
                this.region = regions;
            }
        }


        assertThat(VALIDATOR.validate(new Former(Country.of(CS)))).hasSize(1);

        assertThat(VALIDATOR.validate(new Former(Country.of(FormerlyAssignedCountryCode.CSXX)))).hasSize(0);

        assertThat(VALIDATOR.validate(new Former(Country.of(NL)))).hasSize(1);

        assertThat(VALIDATOR.validate(new Former(UserAssignedCountry.ZZ))).hasSize(1);

        testAsStreamFilter(
            regionValidatorService.fromProperty(Former.class, "region"),
            Former::new);
    }

    void testAsStreamFilter(
        Predicate<Object> predicate,
        Function<Region, Object> instantiator,
        String... assertToContain) throws NoSuchFieldException {

        List<Region> validValues = RegionService.getInstance().values()
            .filter(predicate)
            .sorted(Regions.sortByName(LanguageCode.nl))
            .collect(Collectors.toList());
        for(Region r : validValues) {
            assertThat(VALIDATOR.validate(instantiator.apply(r))).withFailMessage(r + " was supposed to be valid, but is invalid").hasSize(0);
        }
        for (String code : assertToContain) {
            assertThat(validValues.stream().filter(r -> r.getCode().equals(code)).findFirst()).isPresent();
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
