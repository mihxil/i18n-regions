package org.meeuw.i18n.countries.persistence;

import java.util.Locale;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.meeuw.i18n.regions.Region;
import org.meeuw.i18n.regions.persistence.RegionToStringConverter;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */

public class RegionToStringConverterTest {

    // The existing values in POMS.
    private static String[] examples = {
         "AD",
 "AF",
 "AN",
 "AR",
 "AT",
 "AU",
 "AW",
 "BD",
 "BE",
 "BF",
 "BO",
 "BQ",
 "BR",
 "BT",
 "CA",
 "CD",
 "CG",
 "CH",
 "CL",
 "CN",
 "CSHH",
 "CU",
 "CW",
 "CZ",
 "DE",
 "DK",
 "EE",
 "ES",
 "ET",
 "FI",
 "FR",
 "GB",
 "GL",
 "HK",
 "HR",
 "HU",
 "ID",
 "IE",
 "IL",
 "IN",
 "IR",
 "IS",
 "IT",
 "JP",
 "KE",
 "KH",
 "KR",
 "LU",
 "LV",
 "MX",
 "NG",
 "NL",
 "NO",
 "NP",
 "NZ",
 "PK",
 "PL",
 "PS",
 "PT",
 "RO",
 "RS",
 "RU",
 "RW",
 "SE",
 "SG",
 "SI",
 "SN",
 "SR",
 "SX",
 "SY",
 "UA",
 "UG",
 "UM",
 "US",
 "ZA",
 "ZZ"
    };

    public static String[] persistedCountryValues() {
        return examples;
    }

    private RegionToStringConverter impl = new RegionToStringConverter();



    @ParameterizedTest
    @MethodSource("persistedCountryValues")
    public void convertToEntityAttribute(String code) {
        Region region = impl.convertToEntityAttribute(code);
        assertThat(region).isNotNull();
        System.out.println(region + " " + region.getName(new Locale("nl")));
        if (code.equals("AN")) {
            // Antillen does not exist any more.
            assertThat(impl.convertToDatabaseColumn(region)).isEqualTo("ANHH");
        } else {
            assertThat(impl.convertToDatabaseColumn(region)).isEqualTo(code);
        }
    }
}
