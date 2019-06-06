package org.meeuw.i18n;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public interface RegionProvider<T extends Region> {

    Optional<T> getByCode(String code);

    Stream<T> values();
}
