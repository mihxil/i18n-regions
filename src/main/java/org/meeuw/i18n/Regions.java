package org.meeuw.i18n;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.annotation.Nonnull;

import com.neovisionaries.i18n.LanguageCode;

/**
 * Utilities related to {@link Region}s. This also implements the java {@link ServiceLoader}
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class Regions {

    /**
     * Searches all available {@link RegionProvider}s for a region with given code and class.
     * @param code The (ISO) code
     * @param clazz The subclass or sub interface of {@link Region} which is searched
     * @param <T>
     * @return an optional of region. Empty if not found.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Region> Optional<T> getByCode(@Nonnull String code, @Nonnull Class<T> clazz) {
        ServiceLoader<RegionProvider> loader = ServiceLoader.load(RegionProvider.class);
        for (RegionProvider<T> provider : loader) {
            if (provider.canProvide(clazz)) {
                Optional<? extends Region> byCode = provider.getByCode(code);
                if (byCode.isPresent()) {
                    if (clazz.isInstance(byCode.get())) {
                        return (Optional<T>) byCode;
                    }
                }
            }

        }
        return Optional.empty();

    }
     /**
     * Searches all available {@link RegionProvider}s for a region with given code and class. This is a defaulting
      * version of {@link #getByCode(String, Class)}, where the second argument is {@link Region}, and hence it will search Regions of all types.
     * @return an optional of region. Empty if not found.
     */
    public static Optional<Region> getByCode(String s) {
        return getByCode(s, Region.class);
    }

    public static  <T extends Region> Stream<T> values(Class<T> clazz) {
        final ServiceLoader<RegionProvider> loader = ServiceLoader.load(RegionProvider.class);

        Spliterator<T> spliterator = new Spliterator<T>() {
            private final Iterator<RegionProvider> iterator = loader.iterator();
            private Spliterator<T> values;
            @Override
            public boolean tryAdvance(Consumer<? super T> action) {
                while(values == null || ! values.tryAdvance(action)) {
                    if (iterator.hasNext()) {
                        RegionProvider<? extends Region> rp = iterator.next();
                        if (rp.canProvide(clazz)) {
                            values = (Spliterator<T>) rp.values().spliterator();
                        }
                    } else {
                        return false;
                    }
                }
                return true;
            }

            @Override
            public Spliterator<T> trySplit() {
                return null;

            }

            @Override
            public long estimateSize() {
                return Long.MAX_VALUE;

            }

            @Override
            public int characteristics() {
                return IMMUTABLE;

            }
        };
        return StreamSupport.stream(spliterator, false).filter(clazz::isInstance);
    }

    public static Stream<? extends Region> values(Region.Type... types) {
        Set<Region.Type> set = new HashSet<>(Arrays.asList(types));
        return values().filter(r -> set.contains(r.getType()));
    }

    public static Stream<Region> values() {
        return values(Region.class);
    }

    public static Comparator<Region> sortByName(Locale locale) {
        return new Comparator<Region>() {
            @Override
            public int compare(Region o1, Region o2) {
                return o1.getName(locale).compareTo(o2.getName(locale));
            }
        };
    }

    public static Comparator<Region> sortByName(LanguageCode language) {
        return sortByName(language.toLocale());
    }

    public static String toString(@Nonnull  Region region, @Nonnull  LanguageCode language) {
        StringBuilder builder = new StringBuilder();
        builder.append(region.getCode());
        builder.append(':');
        builder.append(region.getName(language));
        if (region instanceof FormerCountry) {
            builder.append(" (").append(((FormerCountry) region).getValidity()).append(")");
        }
        return builder.toString();
    }
}
