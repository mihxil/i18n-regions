package org.meeuw.i18n.openlocationcode;

import java.net.URI;

import org.junit.jupiter.api.Test;
import org.meeuw.i18n.regions.Region;

import com.google.openlocationcode.OpenLocationCode;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 */
class OpenLocationTest {
    final OpenLocation debilt = new OpenLocation(new OpenLocationCode(52.129490, 5.205140));

    @Test
    public void testToString() {
        assertThat(debilt.toString()).isEqualTo("9F4746H4+Q3");
    }

    @Test
    public void getPlusUrl() {
        assertThat(debilt.getPlusURL()).isEqualTo(URI.create("https://plus.codes/9F4746H4+Q3"));
    }

    @Test
    public void getOpenLocationCode() {
        assertThat(debilt.getOpenLocationCode().getCode()).isEqualTo("9F4746H4+Q3");
    }

    @Test
    public void regionMethods() {
        assertThat(debilt.toLocale()).isNull();
        assertThat(debilt.getType()).isEqualTo(Region.Type.UNDEFINED);
        assertThat(debilt.getName()).isEqualTo("9F4746H4+Q3");
    }


}
