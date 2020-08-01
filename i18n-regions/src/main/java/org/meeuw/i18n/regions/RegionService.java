package org.meeuw.i18n.regions;

import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.annotation.Priority;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.i18n.regions.spi.RegionProvider;

/**
 * The singleton service providing information about the registered regions.
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class RegionService {

    private static final Logger logger = Logger.getLogger(RegionService.class.getName());

    private static final RegionService INSTANCE = new RegionService();

    private boolean inited = false;
    private List<RegionProvider<? extends Region>> providers;

    private RegionService() {

    }

    /**
     * The singleton of this class.
     */
    public static RegionService getInstance() {
        INSTANCE.initIfNeeded();
        return INSTANCE;
    }


     /**
      * Searches all available {@link RegionProvider}s for a region with given code and class.
      * @param code The (ISO) code
      * @param lenient Whether matching should be lenient or strict. Lenient matching may e.g. be case insensitive or matching on former codes
      * @param clazz The subclass or sub interface of {@link Region} which is searched
      * @param checker An extra predicate to which the region must match
      * @param <T> The type of the requested Region type
      *
     * @return an optional of region. Empty if not found.
     */
    @SuppressWarnings("unchecked")
    public <T extends Region> Optional<T> getByCode(@NonNull String code, boolean lenient, @NonNull Class<T> clazz, @NonNull Predicate<Region> checker) {
        for (RegionProvider<?> provider : getProviders()) {
            if (provider.canProvide(clazz)) {
                Optional<? extends Region> byCode = provider.getByCode(code, lenient);
                if (byCode.isPresent()) {
                    if (clazz.isInstance(byCode.get()) && checker.test(byCode.get())) {
                        return (Optional<T>) byCode;
                    }
                }
            }

        }
        return Optional.empty();
    }

    /**
     * A defaulting version of {@link #getByCode(String, boolean, Class, Predicate)}. The predicate is implicitly always true.
     */

    public  <T extends Region> Optional<T> getByCode(@NonNull String code, boolean lenient, @NonNull Class<T> clazz) {
        return getByCode(code, lenient, clazz, (c) -> true);
    }

     /**
      * A defaulting version of {@link #getByCode(String, boolean, Class, Predicate)}. No predicate, lenient matching.
     */
    public  <T extends Region> Optional<T> getByCode(@NonNull String code, @NonNull Class<T> clazz) {
        return getByCode(code, true, clazz);
    }

    public  Optional<Region> getByCode(@NonNull String s, boolean lenient) {
        return getByCode(s, lenient, Region.class);
    }
     /**
     * Searches all available {@link RegionProvider}s for a region with given code and class. This is a defaulting
      * version of {@link #getByCode(String, Class)}, where the second argument is {@link Region}, and hence it will search Regions of all types.
     * @return an optional of region. Empty if not found.
     */
    public  Optional<Region> getByCode(String s) {
        return getByCode(s, true);
    }

    /**
     * Returns a stream of all known instances of Region. Filtered by class.
     *
     * You could also use {@link #values()} and then simply {@link Stream#filter(Predicate)}, but the advantage of this method is that {@link RegionProvider}'s which say they cannot {@link RegionProvider#canProvide(Class)} the given class are not even consulted.
     *
     * @param clazz The class (a subclass or interface of {@link Region}
     * @param <T>
     */
    public  <T extends Region> Stream<T> values(Class<T> clazz) {
        Spliterator<T> spliterator = new Spliterator<T>() {
            private final Iterator<? extends RegionProvider<? extends Region>> iterator = getProviders().iterator();
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


    /**
     * @return A stream of all known {@link Region} instances of one of the given {@link Region.Type}.
     */
    public Stream<? extends Region> values(Region.Type... types) {
        Set<Region.Type> set = new HashSet<>(Arrays.asList(types));
        return values().filter(r -> set.contains(r.getType()));
    }

    /**
     * @return A stream of all known {@link Region} instances.
     */
    public Stream<Region> values() {
        return values(Region.class);
    }

    /**
     * @return The provides currently registered
     */
    public List<RegionProvider<?>> getProviders() {
        return Collections.unmodifiableList(providers);
    }



    private void initIfNeeded() {
        if (! inited) {
            boolean init = false;
            synchronized(this) {
                if (! inited) {
                    final ServiceLoader<RegionProvider> loader = ServiceLoader.load(RegionProvider.class);
                    List<RegionProvider<?>> list = new ArrayList<>();
                    loader.iterator().forEachRemaining(list::add);
                    list.sort(priorityComparator());
                    providers = Collections.unmodifiableList(list);
                    inited = true;
                    init = true;
                }
            }
            if (init) {
                // run in separate thread to avoid dead locks
                ForkJoinPool.commonPool().execute(() ->
                    logger.log(Level.FINE, "RegionService has {0}", providers)
                );
            }
        }

    }


    private static <T> Comparator<T> priorityComparator() {
        return  (o1, o2) -> {
            try {
                final Priority p1 = o1 == null ? null : o1.getClass().getAnnotation(Priority.class);
                final int v1 = p1 != null ? p1.value() : 100;
                final Priority p2 = o2 == null ? null : o2.getClass().getAnnotation(Priority.class);
                final int v2 = p2 != null ? p2.value() : 100;
                return v1 - v2;
            } catch (NoClassDefFoundError ncdfe) {
                logger.log(Level.INFO, "{0}:{1} region services {2} {3} are unordered", new Object[]{ncdfe.getClass(), ncdfe.getMessage(), o1, o2});
                return o1.getClass().getSimpleName().compareTo(o2.getClass().getSimpleName());
            }
        };

    }

}
