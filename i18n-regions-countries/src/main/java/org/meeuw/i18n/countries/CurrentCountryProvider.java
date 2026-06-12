package org.meeuw.i18n.countries;

import java.util.Collections;
import java.util.HashSet;

import jakarta.annotation.Priority;


/**
 * Provides all currently countries that are officially assigned
 *
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Priority(1)
public class CurrentCountryProvider extends AbstractCurrentCountryProvider {
    public CurrentCountryProvider() {
        super(new HashSet<>(Collections.singletonList(Assignment.OFFICIALLY_ASSIGNED)));
    }


}
