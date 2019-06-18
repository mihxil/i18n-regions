package org.meeuw.springutils;

import java.util.Locale;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
@Component
public class LocaleConverter implements Converter<String, Locale> {
    @Override
    public Locale convert(String source) {
        String[]  split = source.split(",");
        for (String s : split) {
            try {
                return Locale.forLanguageTag(s);
            } catch (Exception e) {

            }
        }
        return Locale.getDefault();
    }
}
