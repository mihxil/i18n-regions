package org.meeuw.i18n.persistence;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.meeuw.i18n.Region;
import org.meeuw.i18n.RegionService;

/**
 * How to persist.
 *
 * Uses the iso code.
 *
 * TODO, we might need to prefix other region than Countries, because their codes may not be unique.
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Converter(autoApply = true)
public class RegionToStringConverter implements AttributeConverter<Region, String> {

    @Override
    public String convertToDatabaseColumn(Region region) {
        return region == null ? null : region.getCode();
    }

    @Override
    public Region convertToEntityAttribute(String region) {
        return region == null ? null : RegionService.getInstance().getByCode(region).orElse(null);
    }
}
