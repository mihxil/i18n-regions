package org.meeuw.i18n.regions.persistence;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.meeuw.i18n.regions.Region;
import org.meeuw.i18n.regions.RegionService;

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
    @Nullable
    public String convertToDatabaseColumn(@Nullable Region region) {
        return region == null ? null : region.getCode();
    }

    @Override
    @Nullable
    public Region convertToEntityAttribute(@Nullable String region) {
        return region == null ? null :
            RegionService.getInstance().getByCode(region, true).orElse(null);
    }
}
