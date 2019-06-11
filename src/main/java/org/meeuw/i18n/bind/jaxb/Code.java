package org.meeuw.i18n.bind.jaxb;

import java.util.Optional;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.meeuw.i18n.Region;
import org.meeuw.i18n.Regions;

/**
 * An XML Adapter for Regions. The obvious way to marshal/unmarshal is using {@link Region#getCode()}
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class Code extends XmlAdapter<String, Region> {
    @Override
    public Region unmarshal(String v) {
        return Regions.getByCode(v)
            .orElseThrow(() -> new IllegalArgumentException("No such region " + v));
    }

    @Override
    public String marshal(Region countryCode) {
        return Optional.ofNullable(countryCode)
            .map(Region::getCode)
            .orElse(null);
    }
}
