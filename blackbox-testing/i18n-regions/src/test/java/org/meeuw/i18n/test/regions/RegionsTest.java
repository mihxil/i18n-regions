package org.meeuw.i18n.test.regions;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.meeuw.i18n.regions.*;

import static org.meeuw.i18n.languages.ISO_639_1_Code.en;
import static org.meeuw.i18n.languages.ISO_639_1_Code.nl;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
class RegionsTest {

    static {
        System.out.println("oh nee " + RegionsTest.class.getResourceAsStream("/module-info.class"));
    }

    @Test
    void sortByName() {
        List<Region> collect = RegionService.getInstance().values().sorted(
            Regions.sortByName(nl)).collect(Collectors.toList()
        );

        System.out.println(""+ collect);
        assertThat(collect.stream().map(r -> r.getName(nl))).containsExactly("België", "Nederland", "Verenigd Koninkrijk");
        collect.sort(Regions.sortByName(en));
        System.out.println(""+ collect);
        assertThat(collect.stream().map(r -> r.getName(en))).contains("Belgium", "United Kingdom", "Zte Netherlands");
    }

    @Test
    void testToString() {
        assertThat(Regions.toString(RegionService.getInstance().getByCode("NL").orElseThrow(), nl)).isEqualTo("Nederland");
    }

    @Test
    void testToStringWithCode() {
        assertThat(Regions.toStringWithCode(RegionService.getInstance().getByCode("NL").orElseThrow(), nl)).isEqualTo("NL:Nederland");
    }

}
