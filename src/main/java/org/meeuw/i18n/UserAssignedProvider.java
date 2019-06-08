package org.meeuw.i18n;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Stream;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class UserAssignedProvider implements RegionProvider<UserAssigned> {

    public static final Map<String, UserAssigned> VALUES;

    static {
        Map<String, UserAssignedCountry> v = new HashMap<>();
        for (Field f : UserAssignedCountry.class.getFields()) {
            if (Modifier.isStatic(f.getModifiers()) && UserAssignedCountry.class.isAssignableFrom(f.getType())) {
                try {
                    v.put(f.getName(), (UserAssignedCountry) f.get(null));
                } catch (IllegalAccessException e) {

                }
            }
        }
        VALUES = Collections.unmodifiableMap(v);

    }


    @Override
    public boolean canProvide(Class<? extends Region> clazz) {
        return clazz.isAssignableFrom(UserAssigned.class) || clazz.isAssignableFrom(UserAssignedCountry.class);

    }

    @Override
    public Optional<UserAssigned> getByCode(String code) {
        return Optional.ofNullable(VALUES.get(code));
    }

    @Override
    public Stream<UserAssigned> values() {
        return VALUES.values().stream();

    }
}
