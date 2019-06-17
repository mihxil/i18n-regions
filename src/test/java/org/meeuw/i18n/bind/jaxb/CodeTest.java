package org.meeuw.i18n.bind.jaxb;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.junit.Test;
import org.meeuw.i18n.countries.CurrentCountry;
import org.meeuw.i18n.Region;

import com.neovisionaries.i18n.CountryCode;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class CodeTest {

    @XmlRootElement
    static class A {
        @XmlAttribute
        Region region = new CurrentCountry(CountryCode.NL);
    }

    @Test
    public void unmarshal() {
        A a = JAXB.unmarshal(new StringReader("<a region=\"UK\"/>"), A.class);
        assertThat(a.region).isEqualTo(new CurrentCountry(CountryCode.UK));

    }

    @Test
    public void marshal() {
        A a = new A();
        StringWriter w = new StringWriter();
        JAXB.marshal(a, w);
        assertThat(w.toString()).isXmlEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<a region=\"NL\"/>");
    }
}
