package org.meeuw.i18n.it.some;

import java.util.stream.Stream;

import org.meeuw.i18n.regions.spi.RegionProvider;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
public class SomeRegionProvider implements RegionProvider<SomeRegion> {

    @Override
    public Class<SomeRegion> getProvidedClass() {
        return SomeRegion.class;

    }

    @Override
    public Stream<SomeRegion> values() {
        return Stream.of("GB-ENG", "GB-NIR", "GB-SCT", "GB-WLS").map(SomeRegion::new);

    }
}
