package org.meeuw.i18n;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.neovisionaries.i18n.CountryCode;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class Utils {

    public static CountryCode _getByCode(String s) {
        CountryCode code = CountryCode.getByCode(s);
        if (code == null) {
            return VehicleRegistrationCode.valueOf(s).getCode();
        } else {
            return code;
        }

    }

    public static <T extends Region> T getByCode(String s) {
        ServiceLoader<RegionProvider> loader = ServiceLoader.load(RegionProvider.class);
        Iterator<RegionProvider> iterator = loader.iterator();
        while(iterator.hasNext()) {
            RegionProvider<T> provider = iterator.next();
            Optional<T> byCode = provider.getByCode(s);
            if (byCode.isPresent()) {
                return byCode.get();
            }

        }
        return null;
    }

    public static Stream<? extends Region> values() {
        final ServiceLoader<RegionProvider> loader = ServiceLoader.load(RegionProvider.class);

        Spliterator<Region> spliterator = new Spliterator<Region>() {
            private final Iterator<RegionProvider> iterator = loader.iterator();
            private Spliterator<? extends Region> values;
            @Override
            public boolean tryAdvance(Consumer<? super Region> action) {
                while(values == null || ! values.tryAdvance(action)) {
                    if (iterator.hasNext()) {
                        RegionProvider<? extends Region> rp = iterator.next();
                        values = rp.values().spliterator();
                    } else {
                        return false;
                    }
                }
                return true;
            }

            @Override
            public Spliterator<Region> trySplit() {
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
        return StreamSupport.stream(spliterator, false);
    }

    public static Stream<? extends Region> getByType(Region.Type... types) {
        Set<Region.Type> set = new HashSet<>(Arrays.asList(types));
        return values().filter(r -> set.contains(r.getType()));
    }
}
