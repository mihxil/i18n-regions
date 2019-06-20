package org.meeuw.i18n.validation;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.Test;
import org.meeuw.i18n.Region;
import org.meeuw.i18n.RegionService;
import org.meeuw.i18n.Regions;
import org.meeuw.i18n.formerlyassigned.FormerlyAssignedCountryCode;

import com.neovisionaries.i18n.LanguageCode;

import static com.neovisionaries.i18n.CountryCode.CS;
import static com.neovisionaries.i18n.CountryCode.NL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.i18n.countries.Country.of;
import static org.meeuw.i18n.countries.UserAssignedCountry.ZZ;
import static org.meeuw.i18n.formerlyassigned.FormerlyAssignedCountryCode.CSXX;
import static org.meeuw.i18n.countries.validation.ValidCountry.FORMER;
import static org.meeuw.i18n.countries.validation.ValidCountry.OFFICIAL;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class RegionValidatorTest {

    static class A {
        @ValidRegion(value = OFFICIAL | FORMER, includes = {"GB-ENG", "GB-NIR", "GB-SCT", "GB-WLS"})
        Region region;

        public A(Region r) {
            this.region = r;
        }
    }


    static class AWithList {
        @ValidRegion(value = OFFICIAL | FORMER, includes = {"GB-ENG", "GB-NIR", "GB-SCT", "GB-WLS"})
        List<Region> region;

        public AWithList(Region... r) {
            this.region = Arrays.asList(r);
        }
    }
     static class B {
        @ValidRegion(includes = "ZZ")
        List<Region> region;

        public B(Region... r) {
            this.region = Arrays.asList(r);
        }
    }
    private static final ValidatorFactory FACTORY = Validation.buildDefaultValidatorFactory();
    private static final Validator VALIDATOR = FACTORY.getValidator();


    @Test
    public void isValid() {

        assertThat(VALIDATOR.validate(new A(of(CS)))).hasSize(1);
        assertThat(VALIDATOR.validate(new A(of(CS))).iterator().next().getMessage()).isEqualTo("CS is not a valid country");;



        assertThat(VALIDATOR.validate(new A(of(CSXX)))).hasSize(0);

        assertThat(VALIDATOR.validate(new A(of(NL)))).hasSize(0);


        assertThat(VALIDATOR.validate(new A(ZZ))).hasSize(1);
        //assertThat(VALIDATOR.validate(new A(CountrySubdivision.of(GB, "ENG").orElse(null)))).hasSize(0);
        //assertThat(VALIDATOR.validate(new A(CountrySubdivision.of(GB, "NIR").orElse(null)))).hasSize(0);


    }


    @Test
    public void isValidWithList() {

        assertThat(VALIDATOR.validate(new AWithList(of(NL), of(FormerlyAssignedCountryCode.TPTL)))).hasSize(0);


    }

    @Test
    public void onlyIncludes() {


        assertThat(VALIDATOR.validate(new B(of(CS)))).hasSize(1);

        assertThat(VALIDATOR.validate(new B(of(CSXX)))).hasSize(1);

        assertThat(VALIDATOR.validate(new B(of(NL)))).hasSize(1);

        assertThat(VALIDATOR.validate(new B(ZZ))).hasSize(0);
    }

    @Test
    public void eastTimor() {
        Predicate<Object> predicate = RegionValidator.fromField(A.class, "region");
        assertThat(predicate.test("TPTL")).isTrue();
    }

    @Test
    public void useAsStreamFilter() throws NoSuchFieldException {

        List<Region> collect = RegionService.getInstance().values().filter(RegionValidator.fromField(A.class, "region"))
            .sorted(Regions.sortByName(LanguageCode.nl))
            .collect(Collectors.toList());
        assertThat(collect).doesNotContain(of(CS));
        System.out.println(collect.stream()
            .map(r -> Regions.toStringWithCode(r, LanguageCode.nl))
            .collect(Collectors.joining("\n")));
    }
}
