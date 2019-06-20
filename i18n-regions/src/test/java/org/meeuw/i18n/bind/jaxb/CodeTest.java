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
        Region region = new Country("UK", "United Kingdom");
    }
    static class Country implements Region {
        private final String code;
        private final String name;

        Country(String code, String name) {
            this.code = code;
            this.name = name;
        }

        @Override
        public String getCode() {
            return code;
        }

        @Override
        public Type getType() {
            return Type.COUNTRY;

        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Country country = (Country) o;

            return code.equals(country.code);
        }

        @Override
        public int hashCode() {
            return code.hashCode();
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
