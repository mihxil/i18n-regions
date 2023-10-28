package org.meeuw.i18n.regions.languages;

import org.junit.jupiter.api.Test;
import org.meeuw.i18n.languages.ISO_639_3;

class ISO_639_3Test {

    @Test
    void stream() {
        ISO_639_3.stream().forEach(System.out::println);
    }
}
