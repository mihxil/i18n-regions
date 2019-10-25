package org.meeuw.i18n.openlocationcode;

import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.annotation.Priority;
import javax.validation.constraints.Min;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.meeuw.i18n.spi.RegionProvider;

import com.google.openlocationcode.OpenLocationCode;

import static com.google.openlocationcode.OpenLocationCode.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Priority(100)
public class OpenLocationProvider implements RegionProvider<OpenLocation> {

    private static final Logger logger = Logger.getLogger(OpenLocationProvider.class.getName());


    public static final int CODE_ALPHABET_LENGTH = CODE_ALPHABET.length();
    public static final int CODE_ALPHABET_LENGTH_2 = CODE_ALPHABET_LENGTH * CODE_ALPHABET_LENGTH;

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

    private static @Min(value = 0) int maxLength = 4;


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
        return StreamSupport.stream(OpenLocationCodeSpliterator::new, OpenLocationCodeSpliterator.CHARACTERISTICS, false).map(a -> new OpenLocation(toCode(a)));
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
        long result = 9 * 18;
        for (int i = 1; i < length; i++) {
            result *= CODE_ALPHABET_LENGTH_2;
        }
        return result  + limitForLength(length - 1);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " (" + limitForLength(maxLength) + " codes in stream)";

    }

    static OpenLocationCode toCode(int[] template) {
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

    static int[] templateAt(long position) {
        long numberShorter =  9 * 18;
        long proposal = numberShorter;
        int length = 2;
        while (position > proposal) {
            numberShorter = proposal;
            proposal = numberShorter + CODE_ALPHABET_LENGTH_2  * numberShorter;
            length +=2;
        }
        long positionRelative = position - numberShorter;
        int[] template = new int[length];
        fillTemplate(template, positionRelative);;
        return template;
    }
    static void fillTemplate(int[] template, long position) {
        int i = template.length - 1;
        while (i >= 0) {
            int max = getMax(i);
            int modulo = (int) (position % max);
            template[i] = modulo;
            position -= modulo;
            position /= max;
            i--;
        }

    }
    static int[] advance(int[] template, int step, Consumer<int[]> initter) {
        int i = template.length - 1;
        while(i >= 0) {
            int max = getMax(i);
            int newValue = (template[i] + step);
            template[i] = newValue % max;
            int carry = newValue / max;
            if (carry > 0) {
                step = carry;
                i--;
            } else {
                return template;
            }
        }
        template = new int[template.length + 2];
        initter.accept(template);
        return template;
    }
    static void advance(int[] template, int step) {
        advance(template, step, (t) -> {throw new IllegalStateException();});
    }
    private static int getMax(int positionInTemplate) {
        if (positionInTemplate == 0) {
            // First latitude character can only have first 9 values.
            return 9;
        } else if (positionInTemplate == 1) {
            // First longitude character can only have first 18 values.
            return 18;
        } else {
            return CODE_ALPHABET_LENGTH;
        }

    }

    static long position(int[] template) {
        int factor = 1;
        int result = 0;
        int i = template.length - 1;
        while (i >= 0) {
            int nextExtraFactor = getMax(i);
            result += template[i] * factor;
            factor *= nextExtraFactor;
            i--;


        }
        return result;
    }


    private static class OpenLocationCodeSpliterator implements Spliterator<int[]> {

        static final int CHARACTERISTICS =  DISTINCT | NONNULL | IMMUTABLE;

        static int maxStep;
        static {
            int availableProcessors = Runtime.getRuntime().availableProcessors();
             maxStep = Math.min(availableProcessors, 8);
        }

        long count = 0;
        int[] template = new int[2];
        long lastCount = limitForLength(maxLength);
        int step = 1;
        int loggedAtStep = -1;
        int offset = 0;
        @Override
        public boolean tryAdvance(Consumer<? super int[]> action) {
            if (count > lastCount) {
                return false;
            }
            if (logger.isLoggable(Level.FINE) && loggedAtStep != step) {
                logger.fine("Running in " + Thread.currentThread().getName() + " step: " + step + " offset: " + offset);
                loggedAtStep = step;
            }
            action.accept(template);

            template = advance(template, step, (t) -> {
                advance(t, offset);
            });
            if (template.length / 2 > maxLength) {
                return false;
            }
            count++;
            return true;
        }

        @Override
        @SuppressWarnings("override.return.invalid") // Checker forgot to annotate Spliterator
        @Nullable
        public Spliterator<int[]> trySplit() {
            // split will result in the current spliterator handling the even ones, and the split of one the odd ones
            OpenLocationCodeSpliterator split = new OpenLocationCodeSpliterator();
            if (step >= maxStep) {
                // having it  bigger will result so much threads that it doesn't increase performance
                return null;
            }
            split.lastCount = lastCount;
            split.template = new int[template.length];
            System.arraycopy(template, 0, split.template, 0, template.length);
            split.offset = offset + step;
            split.template = advance(split.template, step, t -> {
                advance(split.template, split.offset);
            });

            step *= 2;
            split.step = step;
            split.count = count;
            return split;
        }

        @Override
        public long estimateSize() {
            return (lastCount  - count) / step;
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
            return step == 1 ? CHARACTERISTICS | SIZED : CHARACTERISTICS;
        }
    };
}
