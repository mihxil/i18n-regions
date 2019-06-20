package org.meeuw.i18n.bind.jaxb;

import java.util.Optional;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.meeuw.i18n.Region;
import org.meeuw.i18n.RegionService;

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
        return RegionService.getInstance().getByCode(v)
            .orElseThrow(() -> new IllegalArgumentException("No such region " + v));
    }

    @Override
    public String marshal(Region countryCode) {
        return Optional.ofNullable(countryCode)
            .map(Region::getCode)
            .orElse(null);
    }
}
