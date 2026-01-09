package org.meeuw;

import java.lang.annotation.Annotation;
import java.util.Locale;

import org.meeuw.i18n.languages.LanguageCode;
import org.meeuw.i18n.regions.RegionService;

/**
 * Use e.g.  java -Dfile.encoding=UTF-8 -jar  target/i18n-regions-tests-simple-0.1-SNAPSHOT.jar   nl
 * @author Michiel Meeuwissen
 * @since ...
 */
public class TestSimple {

    public static void main(String[] argv) {
        String arg1 = argv.length == 0 ? null : argv[0];
        LanguageCode locale = arg1 == null ? LanguageCode.languageCode(Locale.getDefault().getLanguage()) : LanguageCode.languageCode(arg1);
        RegionService.getInstance().values().forEach(v -> {
            v.getEmoji().ifPresent(e -> {
                System.out.print(e + "\t");
            });
            System.out.println(" " + v + ":" + v.getName() + ":" + v.getName(locale));
            System.out.println("class: " + v.getClass().getSimpleName());


            for (Annotation a : v.getClass().getAnnotations()) {
                System.out.println(("annotation : " + a));
            }/*
            ClassUtils.getAllInterfaces(v.getClass()).forEach(c -> {
                System.out.println("interface: " + c.getSimpleName());
                        for (Annotation a : c.getAnnotations()) {
                            // Java 11 simply won't see the annotation if the dependency is missing. Good.
                            System.out.println(("   annotation : " + a));
                        }
                    }
            );*/

        });
    }
}
