package org.meeuw.i18n.bind.jaxb;

import org.meeuw.i18n.Region;
import org.meeuw.i18n.Regions;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Optional;

/**
 * An XML Adapter for Regions. The obvious way to marshal/unmarshal is using {@link Region#getCode()}
 *
 * This is used to annotate {@link Region} itself, but it can also be used in specialized XMLWrappers to represent the code (besides e.g. the <em>name</em> of the country).
 *
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
