package org.meeuw.i18n.countries;

import java.util.stream.Stream;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.i18n.Region;
import org.meeuw.i18n.spi.RegionProvider;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
public class SomeRegionProvider implements RegionProvider<SomeRegion> {
    @Override
    public boolean canProvide(@NonNull Class<? extends Region> clazz) {
        return true;

    }

    @Override
    public Stream<SomeRegion> values() {
        return Stream.of(new SomeRegion("GB-ENG"));

    }
}
