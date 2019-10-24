package org.meeuw.i18n;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.neovisionaries.i18n.LanguageCode;

import static com.neovisionaries.i18n.LanguageCode.en;
import static com.neovisionaries.i18n.LanguageCode.nl;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
class RegionsTest {

    @Test
    void sortByName() {
        List<Region> collect = RegionService.getInstance().values().collect(Collectors.toList());

        collect.sort(Regions.sortByName(nl));
        System.out.println(""+ collect);
        assertThat(collect.stream().map(r -> r.getName(nl))).containsExactly("België", "Nederland", "Verenigd Koninkrijk");
        collect.sort(Regions.sortByName(en));
        System.out.println(""+ collect);
        assertThat(collect.stream().map(r -> r.getName(en))).containsExactly("Belgium", "United Kingdom", "Zte Netherlands");
    }

    @Test
    void testToString() {
        assertThat(Regions.toString(RegionService.getInstance().getByCode("NL").orElseThrow(), LanguageCode.nl)).isEqualTo("Nederland");
    }

    @Test
    void testToStringWithCode() {
        assertThat(Regions.toStringWithCode(RegionService.getInstance().getByCode("NL").orElseThrow(), LanguageCode.nl)).isEqualTo("NL:Nederland");

    }

}
