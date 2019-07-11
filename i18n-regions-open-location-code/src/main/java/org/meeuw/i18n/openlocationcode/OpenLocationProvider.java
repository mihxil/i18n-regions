package org.meeuw.i18n.openlocationcode;

import java.util.Comparator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.annotation.Priority;
import javax.validation.constraints.Min;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.i18n.spi.RegionProvider;

import com.google.openlocationcode.OpenLocationCode;

import static com.google.openlocationcode.OpenLocationCode.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Priority(100)
public class OpenLocationProvider implements RegionProvider<OpenLocation> {

    public static final int CODE_ALPHABET_LENGTH = CODE_ALPHABET.length();


    /**
     * Returns the max length of the {@link OpenLocationCode}s returned by {@link #values()}. The length is defined as the number of pairs of longitude/lattidue digits.
     *
     * So this value is never negative, but may be odd.
     *
     * The default is {@code 4}, resulting in codes exactly ending in {@link OpenLocationCode#SEPARATOR}, which are rectangles the size of a block of houses or so, which results in 10_445_954_562 regions coming from {@link #values()}
     */
    public static int getMaxLength() {
        return maxLength;
    }

    public static void setMaxLength(int maxLength) {
        OpenLocationProvider.maxLength = maxLength;
    }

    @Min(value = 0)
    private static int maxLength = 4;


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
     * You may want to limit using {@link #limitForLength(int)}
     */

    @Override
    public Stream<OpenLocation> values() {
        Spliterator<int[]> spliterator = new Spliterator<int[]>() {
            int length = 1;
            int count = 0;
            int[] template = null;
            @Override
            public boolean tryAdvance(Consumer<? super int[]> action) {
                int arrayLength = length * 2;
                if (template == null) {
                    template = new int[arrayLength];
                } else {
                    boolean advanced = advance(template);
                    if (! advanced) {
                        if (length < maxLength) {
                            length++;
                            arrayLength = length * 2;
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
                return limitForLength(maxLength) - count;

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

    /**
     * How much codes will be generated until code of length.
     *
     * This can be used as a {@link Stream#limit(long)} on {@link #values()} to stop streaming when all codes of the given length have been processed.
     * @param length
     * @return
     */
    public  static long limitForLength(@Min(value = 0) int length) {
        if (length == 0) {
            return 0;
        }
        if (length == 1) {
            return 9 * 18;
        }
        long previous = limitForLength(length - 1);
        return previous + 20 * 20 * previous;
    }

    private static OpenLocationCode toCode(int[] template) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i <= template.length / 2 - 1; i++) {
            builder.append(CODE_ALPHABET.charAt(template[2 * i]));
            builder.append(CODE_ALPHABET.charAt(template[2 * i + 1]));
            if (builder.length() == 8) {
                builder.append(SEPARATOR);
            }
        }
        while (builder.length() < 8) {
            builder.append(PADDING_CHARACTER);
        }
        if (builder.length() == 8) {
            builder.append(SEPARATOR);
        }
        return new OpenLocationCode(builder.toString());
    }

    private static boolean advance(int[] template) {
        int i = template.length - 1;
        while(i >= 0) {
            int max;
            if (i == 0) {
                // First latitude character can only have first 9 values.
                max = 9;
            } else if (i == 1) {
                // First longitude character can only have first 18 values.
                max = 18;
            } else {
                max = CODE_ALPHABET_LENGTH;
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
