package org.meeuw.i18n.countries;

import java.util.Arrays;
import java.util.HashSet;

import javax.annotation.Priority;

import com.neovisionaries.i18n.CountryCode;

/**
 * Provides all currently countries that are officially assigned
 *
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Priority(1)
public class CurrentCountryProvider extends AbstractCurrentCountryProvider {
    public CurrentCountryProvider() {
        super(new HashSet<>(Arrays.asList(CountryCode.Assignment.OFFICIALLY_ASSIGNED)));
    }

}
