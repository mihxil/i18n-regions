package org.meeuw.i18n.countries;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import java.util.stream.Stream;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.i18n.regions.Region;
import org.meeuw.i18n.regions.UserAssignedRegion;
import org.meeuw.i18n.regions.spi.RegionProvider;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 * @see UserAssignedCountry
 */
public class UserAssignedCountryProvider implements RegionProvider<UserAssignedCountry> {

    private static final Logger logger = Logger.getLogger(UserAssignedCountry.class.getName());


    private final Map<String, UserAssignedCountry> values = new ConcurrentHashMap<>();

    {
        Map<String, org.meeuw.i18n.countries.UserAssignedCountry> v = new HashMap<>();
        for (Field f : org.meeuw.i18n.countries.UserAssignedCountry.class.getFields()) {
            if (Modifier.isStatic(f.getModifiers()) && org.meeuw.i18n.countries.UserAssignedCountry.class.isAssignableFrom(f.getType())) {
                try {
                    UserAssignedCountry found = (org.meeuw.i18n.countries.UserAssignedCountry) f.get(null);
                    UserAssignedCountry replaced = register(found);
                    logger.fine("Registered " + found);
                } catch (IllegalAccessException ignored) {

                }
            }
        }
    }


    @Override
    public boolean canProvide(@NonNull Class<? extends Region> clazz) {
        return clazz.isAssignableFrom(UserAssignedRegion.class) || clazz.isAssignableFrom(org.meeuw.i18n.countries.UserAssignedCountry.class);
    }

    @Override
    public Optional<UserAssignedCountry> getByCode(@NonNull String code, boolean lenient) {
        if (lenient && code != null) {
            code = code.toUpperCase();
        }
        return Optional.ofNullable(values.get(code));
    }

    @Override
    public Class<UserAssignedCountry> getProvidedClass() {
        return UserAssignedCountry.class;
    }

    @Override
    public Stream<UserAssignedCountry> values() {
        return values.values().stream();
    }

    public UserAssignedCountry register(UserAssignedCountry country) {
        UserAssignedCountry replaces = values.put(country.getCode(), country);
        if (replaces != null) {
            logger.info("Replaced user assigned country " + replaces + " (" + replaces.getName() + ") with " + country + " (" + country.getName() +")");
        }
        return replaces;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " (" + values().count() + " countries)";
    }
}
