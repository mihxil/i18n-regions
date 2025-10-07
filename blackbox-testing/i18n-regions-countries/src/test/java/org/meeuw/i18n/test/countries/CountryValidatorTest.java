package org.meeuw.i18n.test.countries;

import java.lang.annotation.Retention;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import jakarta.validation.*;
import jakarta.validation.constraints.NotNull;

import org.junit.jupiter.api.Test;
import org.meeuw.i18n.countries.*;
import org.meeuw.i18n.countries.codes.CountryCode;
import org.meeuw.i18n.countries.validation.ValidCountry;
import org.meeuw.i18n.formerlyassigned.FormerlyAssignedCountryCode;
import org.meeuw.i18n.regions.*;
import org.meeuw.i18n.regions.validation.RegionValidatorService;
import org.meeuw.i18n.regions.validation.ValidRegion;
import org.meeuw.i18n.test.some.SomeRegion;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.i18n.countries.codes.CountryCode.*;
import static org.meeuw.i18n.languages.ISO_639_1_Code.nl;

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
    public void combineValidRegionAndValidCountry() {

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


        {
            assertThat(VALIDATOR.validate(new CountryAndRegionsOrUserDefined(Country.of(CS)))).hasSize(1);
            assertThat(VALIDATOR.validate(new CountryAndRegionsOrUserDefined(UserAssignedCountry.ZZ))).hasSize(0);
        }


        testAsStreamFilter(
            regionValidatorService.fromProperty(CountryAndRegions.class, "region"),
            CountryAndRegions::new,
            27,
            "TPTL", "GB-ENG");


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
    public void combineValidRegionAndValidCountryString() {
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
            CountryAndRegionsAsString::new,
            27,
            "TPTL", "GB-ENG");
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
    public void onlyIncludes() {
        class ZZ {
            public final List<Country> region;

            ZZ(Region r) {
                this.region = r instanceof  Country ? Collections.singletonList((Country) r) : null;
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
            ZZ::new,
            310
        );
    }




    @Test
    public void testClasses() {
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
            Former::new,
            280
            );
    }

    public static class CountryAndRegionsAsObject {
        @ValidRegion(includes = {"GB-ENG", "GB-NIR", "GB-SCT", "GB-WLS"})
        @ValidCountry(value = ValidCountry.OFFICIAL | ValidCountry.FORMER)
        public final Object region;

        CountryAndRegionsAsObject(Object r) {
            this.region = r;
        }


    }
    @Test
    public void convert() {
        assertThat(VALIDATOR.validate(new CountryAndRegionsAsObject(UserAssignedCountry.ZZ))).hasSize(1);
        assertThat(VALIDATOR.validate(CountryCode.NL)).hasSize(0);
        assertThat(VALIDATOR.validate(FormerlyAssignedCountryCode.CSXX)).hasSize(0);
        assertThat(VALIDATOR.validate(new Locale("nl", "NL"))).hasSize(0);
        assertThat(VALIDATOR.validate(new Locale("nl"))).hasSize(0);

    }


    @ValidRegion(includes = {"GB-ENG", "GB-NIR", "GB-SCT", "GB-WLS"})
    @ValidCountry(value = ValidCountry.OFFICIAL | ValidCountry.USER_ASSIGNED)
    @Constraint(validatedBy = {})
    @Retention(RUNTIME)
    public @interface  MetaAnnotation {
        String message() default "";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }


    public static class CountryAndRegionsMetaAnnotated {
        @MetaAnnotation
        public final Region region;

        public CountryAndRegionsMetaAnnotated(Region r) {
            this.region = r;
        }
    }

    @Test
    public void meta() {
        assertThat(VALIDATOR.validate(new CountryAndRegionsAsObject(UserAssignedCountry.ZZ))).hasSize(1);

        testAsStreamFilter(
            regionValidatorService.fromProperty(CountryAndRegionsMetaAnnotated.class, "region"),
            CountryAndRegionsMetaAnnotated::new,
            50
        );
    }


    @Test
    public void userAssignedInclude() {
        class A {
            @ValidRegion(includeAssigners = {UserAssignedCountry.ASSIGNER_EU}, classes = {UserAssignedCountry.class})
            public Region country;
        }
        List<Region> valids = new ArrayList<>();
        RegionService.getInstance().values().filter(
            regionValidatorService.fromProperty(A.class, "country")
        ).forEach(c -> {
            valids.add(c);
            assertThat(c).isInstanceOf(UserAssignedCountry.class);
            assertThat(((UserAssignedRegion)c).getAssignedBy()).isEqualTo(UserAssignedCountry.ASSIGNER_EU);
        });
        assertThat(valids.toString()).isEqualTo("[XI, XK, XU]");
    }

    @Test
    public void userAssignedExclude() {
        class A {
            @ValidRegion(excludeAssigners = {UserAssignedCountry.ASSIGNER_WIPO}, classes = {UserAssignedCountry.class})
            public Region country;
        }
        List<Region> valids = new ArrayList<>();
        RegionService.getInstance().values().filter(
            regionValidatorService.fromProperty(A.class, "country")
        ).forEach(c -> {
            valids.add(c);
            assertThat(c).isInstanceOf(UserAssignedCountry.class);
            assertThat(((UserAssignedRegion)c).getAssignedBy()).isNotEqualTo(UserAssignedCountry.ASSIGNER_WIPO);
        });
        assertThat(valids.toString()).isEqualTo("[ZZ, XI, XZ, XK, QU, XU, QO]");

    }

    @Test
    public void codes() {
        class A {
            @ValidCountry(codes = {"NL", "BE"}, classes = {CurrentCountry.class})
            public CountryCode country1;

            @ValidRegion(codes = {"NL", "BE"}, classes = {CurrentCountry.class})
            public CountryCode country2;
            A(CountryCode c) {
                this.country1 = c;
                this.country2 = c;
            }

        }
        assertThat(VALIDATOR.validate(new A(BE))).isEmpty();
        assertThat(VALIDATOR.validate(new A(UK))).hasSize(2);

    }


    @Test
    public void locales() {
        class A {
            @ValidRegion(codes = {"NL", "BE"}, classes = {CurrentCountry.class})
            public Locale locale1;
            @ValidCountry(codes = {"NL", "BE"}, classes = {CurrentCountry.class})
            public Locale locale2;

            A(Locale c) {
                this.locale1 = c;
                this.locale2 = c;
            }

        }
        assertThat(VALIDATOR.validate(new A(new Locale("nl", "BE")))).isEmpty();
        assertThat(VALIDATOR.validate(new A(new Locale("nl", "UK")))).hasSize(2);


    }


    void testAsStreamFilter(
        Predicate<Object> predicate,
        Function<Region, Object> instantiator,
        int expectedInvalidCount,
        String... assertToContain) {

        List<Region> validValues = RegionService.getInstance().values()
            .filter(predicate)
            .sorted(Regions.sortByName(nl))
            .collect(Collectors.toList());
        for(Region r : validValues) {
            assertThat(VALIDATOR.validate(instantiator.apply(r)))
                .withFailMessage(r + " was supposed to be valid, but is invalid").hasSize(0);
        }
        for (String code : assertToContain) {
            assertThat(validValues.stream().filter(r -> r.getCode().equals(code)).findFirst()).isPresent();
        }
        List<Region> invalidValues = RegionService.getInstance().values()
            .filter(predicate.negate())
            .sorted(Regions.sortByName(nl))
            .collect(Collectors.toList());

        assertThat(invalidValues).hasSize(expectedInvalidCount);
        for(Region r : invalidValues) {
            assertThat(VALIDATOR.validate(instantiator.apply(r)).size())
                .withFailMessage(""+ r + " is valid, but expected to be invalid").isGreaterThan(0);
        }
        System.out.println(validValues.stream()
            .map(r -> Regions.toStringWithCode(r, nl))
            .collect(Collectors.joining("\n")));
    }


}
