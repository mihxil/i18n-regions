package org.meeuw.i18n.bind.jaxb;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.junit.Test;
import org.meeuw.i18n.Region;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class CodeTest {

    @XmlRootElement
    static class A {
        @XmlAttribute
        Region region = new Country("NL", "Netherlands");

        @Override
        public String toString() {
            return region.getCode();
        }
    }


    @Test
    public void unmarshal() {
        A a = JAXB.unmarshal(new StringReader("<a region=\"UK\"/>"), A.class);
        assertThat(a.region).isEqualTo(new Country("UK", "uk"));

    }

    @Test
    public void marshal() {
        A a = new A();
        StringWriter w = new StringWriter();
        JAXB.marshal(a, w);
        assertThat(w.toString()).isXmlEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<a region=\"NL\"/>");
    }
}
