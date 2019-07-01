package org.meeuw.i18n.countries;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.i18n.Region;
import org.meeuw.i18n.UserAssignedRegion;
import org.meeuw.i18n.spi.RegionProvider;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class UserAssignedCountryProvider implements RegionProvider<UserAssignedRegion> {

    private static final Map<String, UserAssignedRegion> VALUES;

    static {
        Map<String, org.meeuw.i18n.countries.UserAssignedCountry> v = new HashMap<>();
        for (Field f : org.meeuw.i18n.countries.UserAssignedCountry.class.getFields()) {
            if (Modifier.isStatic(f.getModifiers()) && org.meeuw.i18n.countries.UserAssignedCountry.class.isAssignableFrom(f.getType())) {
                try {
                    v.put(f.getName(), (org.meeuw.i18n.countries.UserAssignedCountry) f.get(null));
                } catch (IllegalAccessException e) {

                }
            }
        }
        VALUES = Collections.unmodifiableMap(v);

    }


    @Override
    public boolean canProvide(@NonNull Class<? extends Region> clazz) {
        return clazz.isAssignableFrom(UserAssignedRegion.class) || clazz.isAssignableFrom(org.meeuw.i18n.countries.UserAssignedCountry.class);

    }

    @Override
    public Optional<UserAssignedRegion> getByCode(@NonNull String code, boolean lenient) {
        if (lenient) {
            code = code.toUpperCase();
        }
        return Optional.ofNullable(VALUES.get(code));
    }

    @Override
    public Class<UserAssignedRegion> getProvidedClass() {
        return UserAssignedRegion.class;

    }

    @Override
    public Stream<UserAssignedRegion> values() {
        return VALUES.values().stream();

    }
}
