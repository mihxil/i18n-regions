package org.meeuw.i18n;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.i18n.spi.RegionProvider;

/**
 * The singleton service providing information about the registered regions.
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class RegionService {


    private static final RegionService INSTANCE = new RegionService();

    private boolean inited = false;
    private List<RegionProvider> providers;
    private RegionService() {

    }
    public static RegionService getInstance() {
        INSTANCE.initIfNeeded();
        return INSTANCE;
    }

    private void initIfNeeded() {
        if (! inited) {
            final ServiceLoader<RegionProvider> loader = ServiceLoader.load(RegionProvider.class);
            List<RegionProvider> list = new ArrayList<>();
            loader.iterator().forEachRemaining(list::add);
            list.sort(RegionProvider.COMPARATOR);
            providers = Collections.unmodifiableList(list);
            inited = true;
        }
    }

    public List<RegionProvider> getProviders() {
        return providers;
    }

     /**
     * Searches all available {@link RegionProvider}s for a region with given code and class.
     * @param code The (ISO) code
     * @param clazz The subclass or sub interface of {@link Region} which is searched
     * @param <T>
     * @return an optional of region. Empty if not found.
     */
    @SuppressWarnings("unchecked")
    public <T extends Region> Optional<T> getByCode(@NonNull String code, @NonNull Class<T> clazz, @NonNull Predicate<Region> checker) {
        for (RegionProvider<?> provider : getProviders()) {
            if (provider.canProvide(clazz)) {
                Optional<? extends Region> byCode = provider.getByCode(code);
                if (byCode.isPresent()) {
                    if (clazz.isInstance(byCode.get()) && checker.test(byCode.get())) {
                        return (Optional<T>) byCode;
                    }
                }
            }

        }
        return Optional.empty();
    }

    public  <T extends Region> Optional<T> getByCode(@NonNull String code, @NonNull Class<T> clazz) {
        return getByCode(code, clazz, (c) -> true);
    }

     /**
     * Searches all available {@link RegionProvider}s for a region with given code and class. This is a defaulting
      * version of {@link #getByCode(String, Class)}, where the second argument is {@link Region}, and hence it will search Regions of all types.
     * @return an optional of region. Empty if not found.
     */
    public  Optional<Region> getByCode(String s) {
        return getByCode(s, Region.class);
    }

    public  <T extends Region> Stream<T> values(Class<T> clazz) {
        final ServiceLoader<RegionProvider> loader = ServiceLoader.load(RegionProvider.class);

        Spliterator<T> spliterator = new Spliterator<T>() {
            private final Iterator<? extends RegionProvider> iterator = getProviders().iterator();
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

    public Stream<? extends Region> values(Region.Type... types) {
        Set<Region.Type> set = new HashSet<>(Arrays.asList(types));
        return values().filter(r -> set.contains(r.getType()));
    }

    public Stream<Region> values() {
        return values(Region.class);
    }


}
