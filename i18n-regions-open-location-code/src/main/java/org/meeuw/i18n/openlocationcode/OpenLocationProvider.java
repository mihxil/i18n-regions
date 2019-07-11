package org.meeuw.i18n.openlocationcode;

import java.util.Comparator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.annotation.Priority;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.i18n.spi.RegionProvider;

import com.google.openlocationcode.OpenLocationCode;

import static com.google.openlocationcode.OpenLocationCode.CODE_ALPHABET;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Priority(100)
public class OpenLocationProvider implements RegionProvider<OpenLocation> {

    public static int MAX_DEPTH = 4;
    @Override
    public Optional<OpenLocation> getByCode(@NonNull String code, boolean lenient) {
        try {
            return Optional.of(new OpenLocation(new OpenLocationCode(code)));
        } catch (IllegalArgumentException iae) {
            return Optional.empty();
        }

    }

    @Override
    public Class<OpenLocation> getProvidedClass() {
        return OpenLocation.class;
    }


    /**
     * Returns all possible open location regions. This is a giant list.
     *
     * It will start with the biggest regions, and incrementally outputs all smaller ones.
     *
     * You may want to limit using {@link #limitForDepth(int)}
     */

    @Override
    public Stream<OpenLocation> values() {
        Spliterator<int[]> spliterator = new Spliterator<int[]>() {
            int depth = 0;
            int count = 0;
            int[] template = null;
            @Override
            public boolean tryAdvance(Consumer<? super int[]> action) {
                int arrayLength = (depth + 1) * 2;
                if (template == null) {
                    template = new int[arrayLength];
                } else {
                    boolean advanced = advance(template, depth);
                    if (! advanced) {
                        if (depth < MAX_DEPTH) {
                            depth++;
                            arrayLength = (depth + 1) * 2;
                            template = new int[arrayLength];
                        } else {
                            return false;
                        }
                    }
                }


                int[] copy = new int[arrayLength];
                System.arraycopy(template, 0, copy, 0, arrayLength);
                action.accept(copy);
                count++;
                return true;
            }

            @Override
            public Spliterator<int[]> trySplit() {
                return null;

            }

            @Override
            public long estimateSize() {
                return limitForDepth(MAX_DEPTH) - count;

            }
            @Override
            public Comparator<? super int[]> getComparator() {
                return new Comparator<int[]>() {
                    @Override
                    public int compare(int[] a, int[] b) {
                        if (a == b) {
                            return 0;
                        }

                        int i = 0;
                        int max = Math.min(a.length, b.length);
                        while (i < max) {
                            if (a[i] != b[i]) {
                                return Integer.compare(a[i], b[i]);
                            }
                            i++;
                        }
                        return a.length - b.length;
                    }

                };
            }
            @Override
            public int characteristics() {
                return ORDERED | DISTINCT | SORTED | SIZED | NONNULL | IMMUTABLE | SUBSIZED;

            }
        };

        return StreamSupport.stream(spliterator, false).map(a -> new OpenLocation(toCode(a)));

    }

    public static long limitForDepth(int depth) {
        if (depth > MAX_DEPTH) {
            throw new IllegalArgumentException();
        }
        if (depth == 0) {
            return 9 * 18;
        } else {
            long previous = limitForDepth(depth - 1);
            return previous + 20 * 20 * previous;
        }
    }

    private static OpenLocationCode toCode(int[] template) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i <= template.length / 2 - 1; i++) {
            builder.append(CODE_ALPHABET.charAt(template[2 * i]));
            builder.append(CODE_ALPHABET.charAt(template[2 * i + 1]));
            if (builder.length() == 8) {
                builder.append(OpenLocationCode.SEPARATOR);
            }
        }
        while (builder.length() < 8) {
            builder.append(OpenLocationCode.PADDING_CHARACTER);
        }
        if (builder.length() == 8) {
            builder.append(OpenLocationCode.SEPARATOR);
        }
        return new OpenLocationCode(builder.toString());
    }

    private static boolean advance(int[] template, int depth) {
        int i = depth * 2 + 1;
        while(i >= 0) {
            int max;
            if (i == 0) {
                // First latitude character can only have first 9 values.
                max = 9;
            } else if (i == 1) {
                // First longitude character can only have first 18 values.
                max = 18;
            } else {
                max = CODE_ALPHABET.length();
            }
            template[i]++;
            if (template[i] == max) {
                template[i] = 0;
                i--;
            } else {
                return true;
            }
        }
        return false;

    }
}
