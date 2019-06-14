package org.meeuw;

import org.meeuw.i18n.Regions;

import java.util.Locale;

/**
 * Use e.g.  java -Dfile.encoding=UTF-8 -jar target/i18n-regions-tests-simple-0.1-SNAPSHOT-jar-with-dependencies.jar  nl
 * @author Michiel Meeuwissen
 * @since ...
 */
public class TestSimple {

	public static void main(String[] argv) {
		String arg1 = argv.length == 0 ? null : argv[0];
		Locale locale = argv.length == 0 ? Locale.getDefault() : new Locale(argv[0]);
		Regions.values().forEach(v -> {
			System.out.println(" " + v + ":" + v.getName() + ":" + v.getName(locale));
		});
	}
}
