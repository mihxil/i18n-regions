package org.meeuw.i18n.openlocationcode;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.assertj.core.api.Assumptions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;

import com.google.openlocationcode.OpenLocationCode;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@TestMethodOrder(MethodOrderer.MethodName.class)
public class OpenLocationProviderTest implements BeforeTestExecutionCallback, AfterTestExecutionCallback {


    private static final Set<String> sequentialSet =  new HashSet<>();
    private static final Set<String> parallelSet =  Collections.newSetFromMap(new ConcurrentHashMap<>());

    private static final AtomicInteger sequentialCount = new AtomicInteger(0);

    private static final int takeWhileLength = 3;
    private static final int hashDiv = 100001;

    private Instant start = Instant.now();

    @Override
    public void afterTestExecution(ExtensionContext extensionContext) {
        System.out.println(extensionContext.getDisplayName() + " " + Duration.between(start, Instant.now()));
    }

    @Override
    public void beforeTestExecution(ExtensionContext extensionContext) {
        start = Instant.now();
    }

    @BeforeEach
    public void setup() {
        OpenLocationProvider.setMaxLength(Math.max(takeWhileLength, 4));
        assertThat(OpenLocationProvider.getMaxLength()).isEqualTo(Math.max(takeWhileLength, 4));
    }

    @Test
    public void test1_valuesSequential() {
        OpenLocationProvider provider = new OpenLocationProvider();
        provider.values()
            .takeWhile(r -> r.getLength() <= takeWhileLength)
            .forEach(r -> {
                sequentialCount.getAndIncrement();
                if (r.hashCode() % hashDiv == 0) {
                    // add some random codes to a set to see if parallel handling results the same
                    sequentialSet.add(r.getCode());
                    //OpenLocationCode.CodeArea area = r.getOpenLocationCode().decode();
                    //System.out.println(r.getLength() + ":" + c + ":" + r + " " + area.getCenterLatitude() + "," + area.getCenterLongitude() + ":" + r.getPlusURL());
                }
                }
            );

        assertThat(sequentialCount.get()).isEqualTo(OpenLocationProvider.limitForLength(takeWhileLength));
        //assertThat(sequentialSet.size()).isEqualTo(OpenLocationProvider.limitForLength(takeWhileLength));

    }


    @Test
    public void test2_valuesParallel() {
        OpenLocationProvider provider = new OpenLocationProvider();

        AtomicInteger parallelCount = new AtomicInteger(0);

        long limit = OpenLocationProvider.limitForLength(takeWhileLength);
        provider.values()
            .parallel()
            .takeWhile((r) -> parallelCount.get() < limit)
            .forEach(r -> {
                if (r.getLength() <= takeWhileLength) {
                    parallelCount.getAndIncrement();

                    if (r.hashCode() % hashDiv == 0) {
                        parallelSet.add(r.getCode());
                        //OpenLocationCode.CodeArea area = r.getOpenLocationCode().decode();
                        //System.out.println(c + ":" + r + " " + area.getCenterLatitude() + "," + area.getCenterLongitude() + ":" + r.getPlusURL());
                    }
                }
                }
            );
        //assertThat(parallelSet.size()).isEqualTo(parallelCount.get());
        assertThat(parallelCount.get()).isEqualTo(OpenLocationProvider.limitForLength(takeWhileLength));



    }
    @Test
    public void test3_valuesCompareSequentialWithParallel() {
        Assumptions.assumeThat(sequentialSet).isNotEmpty();
        Assumptions.assumeThat(parallelSet).isNotEmpty();

        List<String> plist = new ArrayList<>(parallelSet);
        Collections.sort(plist);
        List<String> slist = new ArrayList<>(sequentialSet);
        Collections.sort(slist);
        assertThat(plist).isEqualTo(slist);

    }
    @Test
    public void limitForLength() {
        assertThat(OpenLocationProvider.limitForLength(1)).isEqualTo(9 * 18);
        assertThat(OpenLocationProvider.limitForLength(2)).isEqualTo(9 * 18 + 9 * 18 * 20 * 20);
        assertThat(OpenLocationProvider.limitForLength(3)).isEqualTo(9 * 18 + 9 * 18 * 20 * 20 + 9 * 18 * 20 * 20 * 20 * 20);
    }

    @Test
    public void fillTemplate2() {
        int[] template = new int[2];
        OpenLocationProvider.fillTemplate(template, 40);
        assertThat(template).isEqualTo(new int[] {2, 4}); // 40 = 2 * 18 + 4
        String code = OpenLocationProvider.toCode(template).toString();
        assertThat(code).isEqualTo("46000000+"); //
        System.out.println("" + code);

    }
    @Test
    public void fillTemplate4() {
        int[] template = new int[4];
        OpenLocationProvider.fillTemplate(template, 62837);
        String code = OpenLocationProvider.toCode(template).toString();
        assertThat(code).isEqualTo("CM3V0000+"); //
    }

    @Test
    public void templateAt() {
        int[] template = OpenLocationProvider.templateAt(48643);
        String code = OpenLocationProvider.toCode(template).toString();
        assertThat(code).isEqualTo("8M630000+"); //
    }


    @Test
    public void carrying() {
        int[] template = new int[4];
        OpenLocationProvider.fillTemplate(template, 48643);
        assertThat(OpenLocationProvider.position(template)).isEqualTo(48643);
        OpenLocationProvider.advance(template, 1);
        assertThat(OpenLocationProvider.position(template)).isEqualTo(48643 + 1);
        OpenLocationProvider.advance(template, 999);
        assertThat(OpenLocationProvider.position(template)).isEqualTo(48643 + 1 + 999);
    }

    @Test
    public void byCodeAndDetails() {
        OpenLocationProvider provider = new OpenLocationProvider();

        Optional<OpenLocation> byCode = provider.getByCode("CM3V0000+", false);
        assertThat(byCode).contains(new OpenLocation(new OpenLocationCode("CM3V0000+")));
        assertThat(byCode.get().getClass()).isEqualTo(provider.getProvidedClass());

        Optional<OpenLocation> unfound = provider.getByCode("CM3Vxxx+", false);
        assertThat(unfound).isNotPresent();

        assertThat(provider.toString()).isEqualTo("OpenLocationProvider (10393984962 codes in stream)");
    }


}
