package org.meeuw.i18n.countries;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.annotation.Priority;

import org.meeuw.i18n.countries.codes.CountryCode;


/**
 *  Provides all countries backed by {@link CountryCode} but without an official code.
 * <p>
 *  Normally the country does not exist, or does not exist anymore.
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Priority(50)
public class UnofficialCurrentCountryProvider extends AbstractCurrentCountryProvider {

    private static final Set<Assignment> excludes = Set.of(Assignment.OFFICIALLY_ASSIGNED, Assignment.USER_ASSIGNED);
    public UnofficialCurrentCountryProvider() {
        super(
            Arrays.stream(Assignment.values())
                .filter(a -> ! excludes.contains(a))
                .collect(Collectors.toSet())
        );
    }
}
