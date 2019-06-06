package org.meeuw.i18n;

import java.util.Optional;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public interface RegionProvider<T extends Region> {

    Optional<T> getByCode(String code);
}
