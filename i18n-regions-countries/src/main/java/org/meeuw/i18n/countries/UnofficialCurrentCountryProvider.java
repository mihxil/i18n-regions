package org.meeuw.i18n.countries;

import java.util.Arrays;
import java.util.stream.Collectors;

import javax.annotation.Priority;

import com.neovisionaries.i18n.CountryCode;

/**
 *  Provides all countries backed by {@link com.neovisionaries.i18n.CountryCode} but without an official code.
 *
 *  Normally the country does not exist, or does not exist any more.
 *
 *  *
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Priority(50)
public class UnofficialCurrentCountryProvider extends AbstractCurrentCountryProvider {
    public UnofficialCurrentCountryProvider() {
        super(Arrays.stream(CountryCode.Assignment.values()).filter(a -> a != CountryCode.Assignment.OFFICIALLY_ASSIGNED).collect(Collectors.toSet()));
    }
}
