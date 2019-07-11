package org.meeuw.i18n.openlocationcode;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import com.google.openlocationcode.OpenLocationCode;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class OpenLocationProviderTest {

    @Test
    public void values() {
        OpenLocationProvider provider = new OpenLocationProvider();
        System.out.println(OpenLocationProvider.limitForLength(4));
        AtomicInteger i = new AtomicInteger(0);
        provider.values()
            .limit(OpenLocationProvider.limitForLength(2))
            //.skip(OpenLocationProvider.limitForLength(1))
            .forEach(r -> {
                OpenLocationCode.CodeArea area = r.getOpenLocationCode().decode();
                System.out.println(i.incrementAndGet() + ":" + r + " " + area.getCenterLatitude() + "," + area.getCenterLongitude() + ":" + r.getPlusURL());
            }
        );

    }
}
