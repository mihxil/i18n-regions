package org.meeuw.i18n.openlocationcode;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import com.google.openlocationcode.OpenLocationCode;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class OpenLocationProviderTest {

    @Test
    public void values() {
        OpenLocationProvider provider = new OpenLocationProvider();
        OpenLocationProvider.setMaxLength(2);


        Set<String> parallelSet =  Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
        AtomicInteger parallelCount = new AtomicInteger(0);

        { //parallel
            provider.values()
                .parallel()
                .forEach(r -> {

                    long c = parallelCount.getAndIncrement();
                    parallelSet.add(r.getCode());
                    if (c % 100 == 0) {
                        OpenLocationCode.CodeArea area = r.getOpenLocationCode().decode();
                        System.out.println(Thread.currentThread().getName() + ":" + c + ":" + r + " " + area.getCenterLatitude() + "," + area.getCenterLongitude() + ":" + r.getPlusURL());
                    }
                }
                );
        }
        Set<String> sequentialSet =  new HashSet<>();
        AtomicInteger sequentialCount = new AtomicInteger(0);


        {
            provider.values()
                .forEach(r -> {
                        sequentialSet.add(r.getCode());
                        sequentialCount.getAndIncrement();
                    }
                );

        }
        assertThat(sequentialCount.get()).isEqualTo(OpenLocationProvider.limitForLength(OpenLocationProvider.getMaxLength()));
        assertThat(sequentialSet.size()).isEqualTo(OpenLocationProvider.limitForLength(OpenLocationProvider.getMaxLength()));
        assertThat(parallelSet).hasSameElementsAs(sequentialSet);
        assertThat(parallelCount.get()).isEqualTo(sequentialCount.get());

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
}
