package org.meeuw.i18n.persistence;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.meeuw.i18n.Region;
import org.meeuw.i18n.Utils;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Converter
public class RegionToStringConverter implements AttributeConverter<Region, String> {

    @Override
    public String convertToDatabaseColumn(Region region) {
        return region == null ? null : region.getISOCode();
    }

    @Override
    public Region convertToEntityAttribute(String region) {
        return region == null ? null : Utils.getByCode(region);
    }
}
