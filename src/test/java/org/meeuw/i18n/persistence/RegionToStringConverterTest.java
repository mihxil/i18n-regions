package org.meeuw.i18n.persistence;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.assertj.core.api.Assertions.*;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
@RunWith(Parameterized.class)
public class RegionToStringConverterTest {

    static String[] examples = {
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

    @Parameterized.Parameters
    public static Object[] getParameters() {
        return examples;
    }

    RegionToStringConverter impl = new RegionToStringConverter();
    String code;

    public RegionToStringConverterTest(String code) {
        this.code = code;
    }


    @Test
    public void convertToEntityAttribute() {
        assertThat(impl.convertToEntityAttribute(code)).isNotNull();
    }
}
